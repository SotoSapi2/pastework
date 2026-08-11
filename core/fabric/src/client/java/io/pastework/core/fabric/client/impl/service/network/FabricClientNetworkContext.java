package io.pastework.core.fabric.client.impl.service.network;

import io.pastework.core.api.common.service.network.NetworkThreadType;
import io.pastework.core.fabric.common.impl.service.network.AbstractFabricPacketContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.ClientboundPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public final class FabricClientNetworkContext extends AbstractFabricPacketContext
{
    public static FabricClientNetworkContext fromPlayContext(
        NetworkThreadType threadType,
        ClientPlayNetworking.Context context
    )
    {
        return new FabricClientNetworkContext(
            PacketFlow.SERVERBOUND,
            threadType,
            context.player().connection.getConnection(),
            context.player().connection,
            context.player(),
            context.responseSender()
        );
    }

    public static FabricClientNetworkContext fromConfigContext(
        NetworkThreadType threadType,
        ClientConfigurationNetworking.Context context
    )
    {
        ClientPacketListener listener = Objects.requireNonNull(context.client().getConnection());
        return new FabricClientNetworkContext(
            PacketFlow.SERVERBOUND,
            threadType,
            listener.getConnection(),
            listener,
            null,
            context.responseSender()
        );
    }

    private FabricClientNetworkContext(
        PacketFlow packetFlow,
        NetworkThreadType threadType,
        Connection connection,
        PacketListener packetListener,
        @Nullable Player player,
        PacketSender packetSender
    )
    {
        super(packetFlow, threadType, connection, packetListener, player, packetSender);
    }

    @Override
    public boolean canAccept(CustomPacketPayload.@NonNull Type<?> type)
    {
        if(getListener() instanceof ClientboundPacketListener)
        {
            return ClientPlayNetworking.canSend(type);
        }

        if(getListener() instanceof ClientConfigurationPacketListenerImpl)
        {
            return ClientConfigurationNetworking.canSend(type);
        }

        return false;
    }
}
