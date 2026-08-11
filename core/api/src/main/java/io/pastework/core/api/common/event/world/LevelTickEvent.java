package io.pastework.core.api.common.event.world;

import io.pastework.core.api.common.event.Event1;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;

/**
 * Contains ticking update events that are triggered every time level (or world dimension) process ticks.
 */
@ApiStatus.NonExtendable
public interface LevelTickEvent
{
    /**
     * Fired before the level process ticks.
     * <p>
     * This event runs on both Client and Server logical side.
     */
    Event1<Level> PRE = new Event1<>();

    /**
     * Fired after the level process ticks.
     * <p>
     * This event runs on both Client and Server logical side.
     */
    Event1<Level> POST = new Event1<>();
}
