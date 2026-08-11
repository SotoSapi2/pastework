package io.pastework.core.neoforge.impl.service.common.network;

import io.pastework.core.api.Pastework;
import io.pastework.core.api.common.service.network.IConfigurationNetworkChannel;
import io.pastework.core.api.common.service.network.INetworkRegistry;
import io.pastework.core.api.common.service.network.IPlayNetworkChannel;
import io.pastework.core.neoforge.NeoPasteworkEntrypoint;
import io.pastework.core.neoforge.impl.service.INeoEventBusDependant;
import net.minecraft.network.protocol.PacketFlow;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NeoNetworkRegistry implements INetworkRegistry, INeoEventBusDependant
{
    private final NeoPlayNetworkChannel clientPlayChannel;
    private final NeoPlayNetworkChannel serverPlayChanel;
    private final NeoConfigurationNetworkChannel clientConfigurationChannel;

    public NeoNetworkRegistry(NetworkHolderRecord networkHolderRecord)
    {
        clientPlayChannel = new NeoPlayNetworkChannel(
            HandlerThread.MAIN,
            PacketFlow.CLIENTBOUND,
            networkHolderRecord.clientHolder()
        );

        serverPlayChanel = new NeoPlayNetworkChannel(
            HandlerThread.MAIN,
            PacketFlow.SERVERBOUND,
            networkHolderRecord.serverHolder()
        );

        clientConfigurationChannel = new NeoConfigurationNetworkChannel(
            HandlerThread.MAIN,
            PacketFlow.CLIENTBOUND,
            networkHolderRecord.clientHolder()
        );
    }

    @Override
    public IPlayNetworkChannel forServerPlay()
    {
        return serverPlayChanel;
    }

    @Override
    public IPlayNetworkChannel forClientPlay()
    {
        return clientPlayChannel;
    }

    @Override
    public IConfigurationNetworkChannel forClientConfig()
    {
        return clientConfigurationChannel;
    }

    @SubscribeEvent
    private void onNetworkRegistry(RegisterPayloadHandlersEvent event)
    {
        final PayloadRegistrar registrar = event.registrar(NeoPasteworkEntrypoint.MOD_ID);

        serverPlayChanel.onNetworkRegistry(registrar);
        clientPlayChannel.onNetworkRegistry(registrar);
        clientConfigurationChannel.onNetworkRegistry(registrar);
    }

    @SubscribeEvent
    private void onNetworkConfiguration(RegisterConfigurationTasksEvent event)
    {
        clientConfigurationChannel.onNetworkConfiguration(event);
    }

    @Override
    public void handleEventBus(IEventBus eventBus)
    {
        eventBus.register(this);
    }
}
