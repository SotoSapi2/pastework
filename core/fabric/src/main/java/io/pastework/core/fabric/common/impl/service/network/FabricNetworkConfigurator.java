package io.pastework.core.fabric.common.impl.service.network;

import io.pastework.core.base.common.impl.service.network.AbstractNetworkConfigurator;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.jspecify.annotations.NonNull;

public final class FabricNetworkConfigurator extends AbstractNetworkConfigurator
{
    public FabricNetworkConfigurator(
        Connection connection,
        ServerConfigurationPacketListener listener
    )
    {
        super(connection, listener);
    }

    @Override
    public boolean canAccept(CustomPacketPayload.@NonNull Type<?> type)
    {
        if(getListener() instanceof ServerConfigurationPacketListenerImpl listener)
        {
            return ServerConfigurationNetworking.canSend(listener, type);
        }

        return false;
    }
}
