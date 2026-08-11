package io.pastework.core.api.common.event;

/**
 * Represents that event context runs on both Client and Server logical side.
 */
public interface ISidedEventContext
{
    boolean isClientSide();

    default boolean isServerSide()
    {
        return !isClientSide();
    }
}
