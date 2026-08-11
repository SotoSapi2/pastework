package io.pastework.core.fabric.common.impl.service.network;

import io.pastework.core.api.common.service.network.IPlayClientRemote;
import io.pastework.core.base.common.hook.ServerHooks;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public final class FabricPlayClientRemote implements IPlayClientRemote
{
    @Override
    public @Nullable MinecraftServer getServer()
    {
        return ServerHooks.getServer();
    }

    @Override
    public void sendTo(ServerPlayer player, CustomPacketPayload payload)
    {
        ServerPlayNetworking.send(player, payload);
    }

    @Override
    public void sendToAllOf(Collection<ServerPlayer> players, CustomPacketPayload payload)
    {
        for (ServerPlayer player : players)
        {
            ServerPlayNetworking.send(player, payload);
        }
    }
}
