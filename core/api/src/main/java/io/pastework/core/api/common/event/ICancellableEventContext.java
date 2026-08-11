package io.pastework.core.api.common.event;

/**
 * Represents event context can be cancelled.
 */
public interface ICancellableEventContext
{
    boolean isCancelled();

    void setCancelled(boolean cancelled);

    default void cancel()
    {
        setCancelled(true);
    }
}
