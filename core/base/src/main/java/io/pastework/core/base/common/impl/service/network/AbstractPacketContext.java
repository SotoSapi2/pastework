package io.pastework.core.base.common.impl.service.network;

import io.pastework.core.api.common.service.network.IPacketContext;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPacketContext implements IPacketContext
{
    private final PacketFlow packetFlow;
    private final Connection connection;
    private final @Nullable Player player;
    private final PacketListener packetListener;

    protected AbstractPacketContext(
        PacketFlow packetFlow,
        Connection connection,
        PacketListener packetListener,
        @Nullable Player player
    )
    {
        this.packetFlow = packetFlow;
        this.connection = connection;
        this.packetListener = packetListener;
        this.player = player;
    }

    @Override
    public PacketFlow getPacketFlow()
    {
        return packetFlow;
    }

    @Override
    public Connection getConnection()
    {
        return connection;
    }

    @Override
    public PacketListener getListener()
    {
        return packetListener;
    }

    @Override
    public Player getPlayer() throws UnsupportedOperationException
    {
        if(player == null)
        {
            throw new UnsupportedOperationException();
        }

        return player;
    }
}
