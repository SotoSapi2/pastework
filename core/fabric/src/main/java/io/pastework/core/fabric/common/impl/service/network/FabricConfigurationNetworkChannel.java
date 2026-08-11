package io.pastework.core.fabric.common.impl.service.network;

import io.pastework.core.api.common.service.network.IConfigurationNetworkChannel;
import io.pastework.core.api.common.service.network.INetworkConfigurator;
import io.pastework.core.api.common.service.network.PacketInfo;
import io.pastework.core.base.common.impl.service.network.AbstractNetworkChannel;
import io.pastework.core.fabric.common.impl.service.IFabricRegistrable;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.mixin.networking.accessor.ServerCommonPacketListenerImplAccessor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.jspecify.annotations.NonNull;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public final class FabricConfigurationNetworkChannel extends AbstractNetworkChannel<FriendlyByteBuf> implements
    IConfigurationNetworkChannel,
    IFabricRegistrable
{
    private final Queue<Consumer<INetworkConfigurator>> configuratorQueue = new LinkedBlockingQueue<>();

    public FabricConfigurationNetworkChannel(PacketFlow packetFlow)
    {
        super(packetFlow);
    }

    @Override
    public void enqueueConfiguration(@NonNull Consumer<INetworkConfigurator> consumer)
    {
        configuratorQueue.add(consumer);
    }

    @Override
    public boolean isRegistrationFinalized()
    {
        return super.isRegistrationFinalized();
    }

    @Override
    public void processRegistration()
    {
        if(isRegistrationFinalized())
        {
            throw new IllegalStateException();
        }

        for(var packetInfo : getRegisteredPackets())
        {
            registerPacket(packetInfo);
        }

        ServerConfigurationConnectionEvents.CONFIGURE.register(
            this::onConfigureConnection
        );

        finalizeRegistration();
    }

    private void onConfigureConnection(
        ServerConfigurationPacketListenerImpl handler,
        MinecraftServer server
    )
    {
        var accessor = (ServerCommonPacketListenerImplAccessor) handler;
        while (!configuratorQueue.isEmpty())
        {
            Consumer<INetworkConfigurator> consumer = configuratorQueue.poll();
            if(consumer != null)
            {
                INetworkConfigurator configurator = new FabricNetworkConfigurator(
                    accessor.getConnection(),
                    handler
                );

                consumer.accept(configurator);

                for(var task : configurator.getRegisteredTasks())
                {
                    handler.addTask(task);
                }
            }
        }
    }

    private <_TPayload extends CustomPacketPayload>
    void registerPacket(
        PacketInfo<_TPayload, FriendlyByteBuf> packetInfo
    )
    {
        throwIfPacketOppositeFlow(packetInfo);

        // DO NOT use switch case!
        // it's cursed in production build and the runtime will ignore every case given.
        if(packetInfo.getPacketFlow() == PacketFlow.SERVERBOUND)
        {
            PayloadTypeRegistry.configurationC2S()
                .register(packetInfo.getType(), packetInfo.getCodec());

            return;
        }

        if(packetInfo.getPacketFlow() == PacketFlow.CLIENTBOUND)
        {
            PayloadTypeRegistry.configurationS2C()
                .register(packetInfo.getType(), packetInfo.getCodec());

            return;
        }

        throw new UnsupportedOperationException(
            "No implementation for network bound type: " + packetInfo.getPacketFlow()
        );
    }
}
