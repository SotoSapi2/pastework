package io.pastework.core.api.common.event.world;

import io.pastework.core.api.common.event.AbstractEventContext;
import io.pastework.core.api.common.event.ISidedEventContext;
import lombok.Getter;
import net.minecraft.world.level.Level;

/**
 * Base context for generic world events.
 */
public abstract class AbstractWorldEventContext extends AbstractEventContext implements ISidedEventContext
{
    @Getter
    private final Level level;

    @Override
    public boolean isClientSide()
    {
        return level.isClientSide();
    }

    protected AbstractWorldEventContext(Level level)
    {
        this.level = level;
    }
}
