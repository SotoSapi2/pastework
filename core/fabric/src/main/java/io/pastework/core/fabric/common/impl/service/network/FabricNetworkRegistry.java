package io.pastework.core.fabric.common.impl.service.network;


import io.pastework.core.api.common.service.network.IConfigurationNetworkChannel;
import io.pastework.core.api.common.service.network.INetworkRegistry;
import io.pastework.core.api.common.service.network.IPlayNetworkChannel;
import io.pastework.core.fabric.common.impl.service.IFabricRegistrable;
import net.minecraft.network.protocol.PacketFlow;

public final class FabricNetworkRegistry implements INetworkRegistry, IFabricRegistrable
{
    private final FabricPlayNetworkChannel clientPlayChannel;
    private final FabricPlayNetworkChannel serverPlayChannel;
    private final FabricConfigurationNetworkChannel configurationChannel;
    private volatile boolean isRegistrationFinalized;

    public FabricNetworkRegistry()
    {
        clientPlayChannel = new FabricPlayNetworkChannel(PacketFlow.CLIENTBOUND);
        serverPlayChannel = new FabricPlayNetworkChannel(PacketFlow.SERVERBOUND);
        configurationChannel = new FabricConfigurationNetworkChannel(PacketFlow.CLIENTBOUND);
    }

    @Override
    public boolean isRegistrationFinalized()
    {
        return isRegistrationFinalized;
    }

    @Override
    public void processRegistration()
    {
        clientPlayChannel.processRegistration();
        serverPlayChannel.processRegistration();
        configurationChannel.processRegistration();

        isRegistrationFinalized = true;
    }

    @Override
    public IPlayNetworkChannel forServerPlay()
    {
        return serverPlayChannel;
    }

    @Override
    public IPlayNetworkChannel forClientPlay()
    {
        return clientPlayChannel;
    }

    @Override
    public IConfigurationNetworkChannel forClientConfig()
    {
        return configurationChannel;
    }
}
