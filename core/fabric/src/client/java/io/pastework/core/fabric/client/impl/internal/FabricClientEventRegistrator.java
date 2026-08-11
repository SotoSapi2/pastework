package io.pastework.core.fabric.client.impl.internal;

import io.pastework.core.base.client.hook.ClientHooks;
import io.pastework.core.base.common.hook.LevelHooks;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class FabricClientEventRegistrator
{
    private static boolean initialized;

    public static void initialize()
    {
        if (initialized)
        {
            throw new IllegalStateException("FabricClientEventRegistrator already initialized");
        }

        ClientTickEvents.START_WORLD_TICK.register(LevelHooks::fireLevelPreTickEvent);
        ClientTickEvents.END_WORLD_TICK.register(LevelHooks::fireLevelPostTickEvent);
        ClientTickEvents.START_CLIENT_TICK.register(ClientHooks::fireClientPreTickEvent);
        ClientTickEvents.END_CLIENT_TICK.register(ClientHooks::fireClientPostTickEvent);

        initialized = true;
    }
}
