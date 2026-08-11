package io.pastework.core.neoforge.impl.service.common.network;

import io.pastework.core.api.common.service.network.IConfigurationNetworkChannel;
import io.pastework.core.api.common.service.network.INetworkConfigurator;
import io.pastework.core.api.common.service.network.PacketInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jspecify.annotations.NonNull;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public final class NeoConfigurationNetworkChannel extends NeoAbstractNetworkChannel<FriendlyByteBuf>
    implements
    IConfigurationNetworkChannel
{
    private final Queue<Consumer<INetworkConfigurator>> configuratorQueue = new LinkedBlockingQueue<>();

    public NeoConfigurationNetworkChannel(
        HandlerThread handlerThread,
        PacketFlow packetFlow,
        NetworkListenerHolder holder
    )
    {
        super(handlerThread, packetFlow, holder);
    }

    @Override
    public void enqueueConfiguration(@NonNull Consumer<INetworkConfigurator> consumer)
    {
        configuratorQueue.add(consumer);
    }

    public void onNetworkConfiguration(RegisterConfigurationTasksEvent event)
    {
        while (!configuratorQueue.isEmpty())
        {
            Consumer<INetworkConfigurator> consumer = configuratorQueue.poll();
            if(consumer != null)
            {
                INetworkConfigurator configurator = new NeoNetworkConfigurator(
                    event.getListener().getConnection(),
                    event.getListener()
                );

                consumer.accept(configurator);

                for(var task : configurator.getRegisteredTasks())
                {
                    event.register(task);
                }
            }
        }
    }

    @Override
    protected
    <_TPacket extends CustomPacketPayload>
    void handleClientboundPacketInfo(
        PayloadRegistrar registrar,
        PacketInfo<_TPacket, FriendlyByteBuf> packetInfo
    )
    {
        registrar.configurationToClient(
            packetInfo.getType(),
            packetInfo.getCodec(),
            (payload, ctx) -> handlePayload(
                packetInfo,
                payload,
                ctx
            )
        );
    }

    @Override
    protected
    <_TPacket extends CustomPacketPayload>
    void handleServerboundPacketInfo(
        PayloadRegistrar registrar,
        PacketInfo<_TPacket, FriendlyByteBuf> packetInfo
    )
    {
        registrar.configurationToServer(
            packetInfo.getType(),
            packetInfo.getCodec(),
            (payload, ctx) -> handlePayload(
                packetInfo,
                payload,
                ctx
            )
        );
    }
}
