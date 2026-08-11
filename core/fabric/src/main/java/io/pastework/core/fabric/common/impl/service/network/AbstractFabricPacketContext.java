package io.pastework.core.fabric.common.impl.service.network;

import io.pastework.core.api.common.service.network.NetworkThreadType;
import io.pastework.core.base.common.impl.service.network.AbstractPacketContext;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public abstract class AbstractFabricPacketContext extends AbstractPacketContext
{
    private final PacketSender packetSender;
    private final NetworkThreadType threadType;

    public AbstractFabricPacketContext(
        PacketFlow packetFlow,
        NetworkThreadType threadType,
        Connection connection,
        PacketListener packetListener,
        @Nullable Player player,
        PacketSender packetSender
    )
    {
        super(packetFlow, connection, packetListener, player);
        this.packetSender = packetSender;
        this.threadType = threadType;
    }

    @Override
    public NetworkThreadType getNetworkThreadType()
    {
        return threadType;
    }

    @Override
    public void reply(@NonNull CustomPacketPayload payload)
    {
        packetSender.sendPacket(payload);
    }
}
