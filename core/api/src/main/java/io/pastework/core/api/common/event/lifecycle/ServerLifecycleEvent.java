package io.pastework.core.api.common.event.lifecycle;

import io.pastework.core.api.common.event.Event1;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ServerLifecycleEvent
{
    Event1<MinecraftServer> STARTING = new Event1<>();

    Event1<MinecraftServer> STARTED = new Event1<>();

    Event1<MinecraftServer> STOPPING = new Event1<>();

    Event1<MinecraftServer> STOPPED = new Event1<>();
}
