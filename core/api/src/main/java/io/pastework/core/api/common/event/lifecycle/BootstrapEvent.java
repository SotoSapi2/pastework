package io.pastework.core.api.common.event.lifecycle;

import io.pastework.core.api.common.event.Event0;
import org.jetbrains.annotations.ApiStatus;

/**
 * Contains events for handling mod bootstrapping phase.
 */
@ApiStatus.NonExtendable
public interface BootstrapEvent
{
    Event0 COMMON_SETUP = new Event0();

    Event0 SERVER_SETUP = new Event0();

    Event0 CLIENT_SETUP = new Event0();

    Event0 LOADING_COMPLETE = new Event0();
}
