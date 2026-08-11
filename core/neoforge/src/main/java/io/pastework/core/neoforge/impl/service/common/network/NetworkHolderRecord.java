package io.pastework.core.neoforge.impl.service.common.network;

import io.pastework.spi.IPasteworkService;
import net.minecraft.network.protocol.PacketFlow;

public record NetworkHolderRecord(NetworkListenerHolder clientHolder, NetworkListenerHolder serverHolder) implements
    IPasteworkService
{
    public static NetworkHolderRecord createDefault()
    {
        return new NetworkHolderRecord(
            new NetworkListenerHolder(PacketFlow.CLIENTBOUND),
            new NetworkListenerHolder(PacketFlow.SERVERBOUND)
        );
    }
}
