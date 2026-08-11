package io.pastework.core.api.common.event;

import org.jetbrains.annotations.ApiStatus;

/**
 * Interface representing an event connection.
 */
@ApiStatus.NonExtendable
public interface IEventConnection
{
    int getPriority();

    void disconnect();

    boolean isConnected();
}
