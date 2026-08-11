package io.pastework.core.api.common.event.world;

import io.pastework.core.api.common.event.AbstractEventContext;
import io.pastework.core.api.common.event.Event1;
import io.pastework.core.api.common.event.Event2;
import lombok.Getter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ServerPlayerEvent
{
    /**
     * Fired when player joins a server or loads a world in singleplayer.
     */
    Event1<ServerPlayer> JOIN = new Event1<>();

    /**
     * Fired when player leaves.
     */
    Event1<ServerPlayer> LEAVE = new Event1<>();

    /**
     * Fired when server player changes level (or world dimension).
     */
    Event2<ChangedDimensionContext, ServerPlayer> CHANGED_LEVEL = new Event2<>();

    /**
     * Fired when server copies the data from an old player to a new player.
     * <p>
     * This event is called after the old player is removed and untracked, but before the new player is
     * added and tracked. Mods may use this event to copy old player data to a new player
     */
    Event2<CloneContext, ServerPlayer> CLONE = new Event2<>();

    /**
     * Fired when server processes player respawn requests.
     */
    Event2<RespawnContext, ServerPlayer> RESPAWN = new Event2<>();

    final class ChangedDimensionContext extends AbstractEventContext
    {
        @Getter
        private final ResourceKey<Level> from;

        @Getter
        private final ResourceKey<Level> to;

        @ApiStatus.Internal
        public ChangedDimensionContext(ResourceKey<Level> from, ResourceKey<Level> to)
        {
            this.from = from;
            this.to = to;
        }
    }

    final class RespawnContext extends AbstractEventContext
    {
        @Getter
        private final boolean endConquered;

        @ApiStatus.Internal
        public RespawnContext(boolean endConquered)
        {
            this.endConquered = endConquered;
        }
    }

    final class CloneContext extends AbstractEventContext
    {
        @Getter
        private final ServerPlayer original;

        @Getter
        private final boolean wasDeath;

        @ApiStatus.Internal
        public CloneContext(ServerPlayer original, boolean wasDeath)
        {
            this.original = original;
            this.wasDeath = wasDeath;
        }
    }
}
