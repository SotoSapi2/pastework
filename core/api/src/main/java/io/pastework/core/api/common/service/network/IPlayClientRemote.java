package io.pastework.core.api.common.service.network;

import io.pastework.core.api.Pastework;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Interface that provides methods to send custom packets from the server to clients.
 */
@ApiStatus.NonExtendable
public interface IPlayClientRemote
{
    /**
     * Retrieves the instance of the remote client handle from the server networking service.
     *
     * @return The play client remote handle.
     */
    static IPlayClientRemote getRemote()
    {
        return Pastework.INSTANCE
            .getService(IServerNetworking.class)
            .getPlayRemote();
    }

    /**
     * Gets the current Minecraft server instance.
     *
     * @return The {@link MinecraftServer} instance, or {@code null} if the server is not available.
     */
    @Nullable MinecraftServer getServer();

    /**
     * Sends a packet payload to a specific player.
     *
     * @param player  The target server player.
     * @param payload The custom packet payload to send.
     */
    void sendTo(ServerPlayer player, CustomPacketPayload payload);

    /**
     * Sends a packet payload to a collection of players.
     *
     * @param players The collection of target server players.
     * @param payload The custom packet payload to send.
     */
    void sendToAllOf(Collection<ServerPlayer> players, CustomPacketPayload payload);

    /**
     * Sends a packet payload to all players currently connected to the server.
     *
     * @param payload The custom packet payload to send.
     */
    default void sendToAll(CustomPacketPayload payload)
    {
        var server = getServer();

        if(server != null)
        {
            sendToAllOf(PlayerLookup.all(server), payload);
        }
    }

    /**
     * Sends a packet payload to all connected players that match the given predicate.
     *
     * @param predicate The predicate used to filter target players.
     * @param payload   The custom packet payload to send.
     */
    default void sendToAllFiltered(Predicate<ServerPlayer> predicate, CustomPacketPayload payload)
    {
        var server = getServer();

        if(server != null)
        {
            sendToAllOf(PlayerLookup.allFiltered(server, predicate), payload);
        }
    }

    /**
     * Sends a packet payload to all players in the specified server level.
     *
     * @param serverLevel The target server level.
     * @param payload     The custom packet payload to send.
     */
    default void sendToAllOn(ServerLevel serverLevel, CustomPacketPayload payload)
    {
        var server = getServer();

        if(server != null)
        {
            sendToAllOf(PlayerLookup.level(serverLevel), payload);
        }
    }

    /**
     * Sends a packet payload to all players tracking the specified entity.
     *
     * @param entity  The entity being tracked.
     * @param payload The custom packet payload to send.
     */
    default void sendToTrackingEntity(Entity entity, CustomPacketPayload payload)
    {
        var server = getServer();

        if(server != null)
        {
            sendToAllOf(PlayerLookup.trackingEntity(entity), payload);
        }
    }

    /**
     * Sends a packet payload to all players tracking the specified chunk.
     *
     * @param level    The server level containing the chunk.
     * @param chunkPos The chunk position.
     * @param payload  The custom packet payload to send.
     */
    default void sendToTrackingChunk(ServerLevel level, ChunkPos chunkPos, CustomPacketPayload payload)
    {
        var server = getServer();

        if(server != null)
        {
            sendToAllOf(PlayerLookup.trackingChunk(level, chunkPos), payload);
        }
    }

    /**
     * Sends a packet payload to all players tracking the chunk containing the specified block position.
     *
     * @param level    The server level containing the block.
     * @param blockPos The block position.
     * @param payload  The custom packet payload to send.
     */
    default void sendToTrackingBlock(ServerLevel level, BlockPos blockPos, CustomPacketPayload payload)
    {
        var server = getServer();

        if(server != null)
        {
            sendToAllOf(PlayerLookup.trackingBlock(level, blockPos), payload);
        }
    }

    /**
     * Sends a packet payload to all players tracking the chunk containing the specified block entity.
     *
     * @param blockEntity The block entity being tracked.
     * @param payload     The custom packet payload to send.
     */
    default void sendToTrackingBlock(BlockEntity blockEntity, CustomPacketPayload payload)
    {
        var server = getServer();

        if(server != null)
        {
            sendToAllOf(PlayerLookup.trackingBlock(blockEntity), payload);
        }
    }

    /**
     * Sends a packet payload to all players within a given radius around a specific position.
     *
     * @param level   The server level.
     * @param pos     The center position.
     * @param radius  The radius around the position.
     * @param payload The custom packet payload to send.
     */
    default void sendToAround(ServerLevel level, Vec3 pos, double radius, CustomPacketPayload payload)
    {
        var server = getServer();

        if(server != null)
        {
            sendToAllOf(PlayerLookup.around(level, pos, radius), payload);
        }
    }

    /**
     * Sends a packet payload to all players within a given radius around a specific integer position.
     *
     * @param level   The server level.
     * @param pos     The integer center position.
     * @param radius  The radius around the position.
     * @param payload The custom packet payload to send.
     */
    default void sendToAround(ServerLevel level, Vec3i pos, double radius, CustomPacketPayload payload)
    {
        var server = getServer();

        if(server != null)
        {
            sendToAllOf(PlayerLookup.around(level, pos, radius), payload);
        }
    }
}
