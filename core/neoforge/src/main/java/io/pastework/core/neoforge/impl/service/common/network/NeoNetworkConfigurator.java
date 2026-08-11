package io.pastework.core.neoforge.impl.service.common.network;

import io.pastework.core.base.common.impl.service.network.AbstractNetworkConfigurator;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.neoforged.neoforge.common.extensions.ICommonPacketListener;
import org.jspecify.annotations.NonNull;

public final class NeoNetworkConfigurator extends AbstractNetworkConfigurator
{
    public NeoNetworkConfigurator(
        Connection connection,
        ServerConfigurationPacketListener listener
    )
    {
        super(connection, listener);
    }

    @Override
    public boolean canAccept(CustomPacketPayload.@NonNull Type<?> type)
    {
        if(getListener() instanceof ICommonPacketListener listenerEx)
        {
            return listenerEx.hasChannel(type);
        }

        throw new UnsupportedOperationException("Packet checking operation is unsupported.");
    }
}
