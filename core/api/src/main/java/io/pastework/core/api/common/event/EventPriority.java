package io.pastework.core.api.common.event;

import lombok.experimental.UtilityClass;

/**
 * Defines standard priority levels for event handlers.
 * Higher values indicate higher priority, meaning the handler will be executed
 * earlier.
 */
@UtilityClass
public final class EventPriority
{
    /**
     * Highest priority. Executed first.
     */
    public static final int HIGHEST = 200;

    /**
     * High priority.
     */
    public static final int HIGH = 100;

    /**
     * Normal priority. This is the default.
     */
    public static final int NORMAL = 0;

    /**
     * Low priority.
     */
    public static final int LOW = -100;

    /**
     * Lowest priority. Executed last.
     */
    public static final int LOWEST = -200;
}
