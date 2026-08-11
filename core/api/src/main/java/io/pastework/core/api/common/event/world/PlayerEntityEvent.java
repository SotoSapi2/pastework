package io.pastework.core.api.common.event.world;

import io.pastework.core.api.common.event.Event1;
import lombok.Getter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface PlayerEntityEvent
{
    /**
     * Fired when player entity died.
     * <p>
     * This event runs on both Client and Server logical side.
     */
    Event1<DiedContext> DIED = new Event1<>();

    /**
     * Fired before player entity process ticks.
     * <p>
     * This event runs on both Client and Server logical side.
     */
    Event1<TickContext> PRE_TICK = new Event1<>();

    /**
     * Fired after player entity process ticks.
     * <p>
     * This event runs on both Client and Server logical side.
     */
    Event1<TickContext> POST_TICK = new Event1<>();

    @Getter
    abstract class AbstractContext extends AbstractWorldEventContext
    {
        private final Player player;

        public AbstractContext(Player player)
        {
            super(player.level());
            this.player = player;
        }

        public boolean isPlayerLocal()
        {
            return player.isLocalPlayer();
        }
    }

    final class DiedContext extends AbstractContext
    {
        @Getter
        private final DamageSource damageSource;

        @ApiStatus.Internal
        public DiedContext(Player player, DamageSource damageSource)
        {
            super(player);
            this.damageSource = damageSource;
        }
    }

    final class TickContext extends AbstractContext
    {
        @ApiStatus.Internal
        public TickContext(Player player)
        {
            super(player);
        }
    }
}
