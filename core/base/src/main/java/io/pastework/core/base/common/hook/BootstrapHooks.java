package io.pastework.core.base.common.hook;

import io.pastework.core.api.common.event.lifecycle.BootstrapEvent;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.ApiStatus;

@UtilityClass
@ApiStatus.Internal
public final class BootstrapHooks
{
    public static void fireCommonSetupEvent()
    {
        BootstrapEvent.COMMON_SETUP.fire();
    }

    public static void fireClientSetupEvent()
    {
        BootstrapEvent.CLIENT_SETUP.fire();
    }

    public static void fireServerSetupEvent()
    {
        BootstrapEvent.SERVER_SETUP.fire();
    }

    public static void fireLoadingCompleteEvent()
    {
        BootstrapEvent.LOADING_COMPLETE.fire();
    }
}
