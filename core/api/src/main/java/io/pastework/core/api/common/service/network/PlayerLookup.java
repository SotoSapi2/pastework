package io.pastework.core.api.common.service.network;

import io.pastework.core.api.common.mixin.accessors.PasteworkEntityTrackerAccessor;
import io.pastework.core.api.common.mixin.accessors.PasteworkMapAccessor;
import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// Based on fabric's PlayerLookup
// https://github.com/FabricMC/fabric-api/blob/26.1.2/fabric-networking-api-v1/src/main/java/net/fabricmc/fabric/api/networking/v1/PlayerLookup.java
@UtilityClass
public final class PlayerLookup
{
    public static Collection<ServerPlayer> all(MinecraftServer server)
    {
        Objects.requireNonNull(server, "The server cannot be null");
        return Collections.unmodifiableList(server.getPlayerList().getPlayers());
    }

    public static Collection<ServerPlayer> allFiltered(MinecraftServer server, Predicate<ServerPlayer> predicate)
    {
        Objects.requireNonNull(server, "The server cannot be null");
        var players = server.getPlayerList()
            .getPlayers()
            .stream()
            .filter(predicate);

        return players.toList();
    }

    public static Collection<ServerPlayer> level(ServerLevel level)
    {
        Objects.requireNonNull(level, "The world cannot be null");
        return Collections.unmodifiableList(level.players());
    }

    public static Collection<ServerPlayer> levelFiltered(ServerLevel level, Predicate<ServerPlayer> predicate)
    {
        Objects.requireNonNull(level, "The world cannot be null");
        var players = level.players()
            .stream()
            .filter(predicate);

        return players.toList();
    }

    public static Collection<ServerPlayer> trackingChunk(ServerLevel level, ChunkPos pos)
    {
        Objects.requireNonNull(level, "The world cannot be null");
        Objects.requireNonNull(pos, "The chunk pos cannot be null");

        return level.getChunkSource().chunkMap.getPlayers(pos, false);
    }

    public static Collection<ServerPlayer> trackingEntity(Entity entity)
    {
        Objects.requireNonNull(entity, "Entity cannot be null");
        ChunkSource manager = entity.level().getChunkSource();

        if (manager instanceof ServerChunkCache)
        {
            ChunkMap chunkLoadingManager = ((ServerChunkCache) manager).chunkMap;
            PasteworkEntityTrackerAccessor tracker = ((PasteworkMapAccessor) chunkLoadingManager).getEntityTrackers()
                .get(entity.getId());

            // return an immutable collection to guard against accidental removals.
            if (tracker != null)
            {
                return tracker.getPlayersTracking()
                    .stream().map(ServerPlayerConnection::getPlayer).collect(Collectors.toUnmodifiableSet());
            }

            return Collections.emptySet();
        }

        throw new IllegalArgumentException("Only supported on server worlds!");
    }

    public static Collection<ServerPlayer> trackingBlock(ServerLevel level, BlockPos pos)
    {
        Objects.requireNonNull(pos, "BlockPos cannot be null");

        return trackingChunk(level, new ChunkPos(pos));
    }

    public static Collection<ServerPlayer> trackingBlock(BlockEntity blockEntity)
    {
        Objects.requireNonNull(blockEntity, "BlockEntity cannot be null");

        //noinspection ConstantConditions - IJ intrinsics don't know hasWorld == true will result in no null
        if (!blockEntity.hasLevel() || blockEntity.getLevel().isClientSide())
        {
            throw new IllegalArgumentException("Only supported on server worlds!");
        }

        return trackingBlock((ServerLevel) blockEntity.getLevel(), blockEntity.getBlockPos());
    }

    public static Collection<ServerPlayer> around(ServerLevel level, Vec3 pos, double radius)
    {
        double radiusSq = radius * radius;

        return level(level)
            .stream()
            .filter((p) -> p.distanceToSqr(pos) <= radiusSq)
            .collect(Collectors.toList());
    }

    public static Collection<ServerPlayer> around(ServerLevel level, Vec3i pos, double radius)
    {
        double radiusSq = radius * radius;

        return level(level)
            .stream()
            .filter((p) -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= radiusSq)
            .collect(Collectors.toList());
    }
}
