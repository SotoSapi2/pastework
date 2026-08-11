package io.pastework.core.neoforge.impl.service.common.network;

import io.pastework.core.api.common.service.network.IPlayClientRemote;
import io.pastework.core.base.common.hook.ServerHooks;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public final class NeoPlayClientRemote implements IPlayClientRemote
{
    @Override
    public @Nullable MinecraftServer getServer()
    {
        return ServerHooks.getServer();
    }

    @Override
    public void sendTo(ServerPlayer player, CustomPacketPayload payload)
    {
        PacketDistributor.sendToPlayer(player, payload);
    }

    @Override
    public void sendToAllOf(Collection<ServerPlayer> players, CustomPacketPayload payload)
    {
        for (ServerPlayer player : players)
        {
            PacketDistributor.sendToPlayer(player, payload);
        }
    }
}
