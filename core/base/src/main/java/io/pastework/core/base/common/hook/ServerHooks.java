package io.pastework.core.base.common.hook;

import io.pastework.core.api.common.event.lifecycle.ServerLifecycleEvent;
import io.pastework.core.api.common.event.network.ServerChatEvent;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@UtilityClass
@ApiStatus.Internal
public final class ServerHooks
{

    @Getter
    private static @Nullable MinecraftServer server;

    public static void fireServerStartingEvent(MinecraftServer server)
    {
        ServerHooks.server = server;
        ServerLifecycleEvent.STARTING.fire(server);
    }

    public static void fireServerStartedEvent(MinecraftServer server)
    {
        ServerLifecycleEvent.STARTED.fire(server);
    }

    public static void fireServerStoppingEvent(MinecraftServer server)
    {
        ServerLifecycleEvent.STOPPING.fire(server);
    }

    public static void fireServerStoppedEvent(MinecraftServer server)
    {
        ServerHooks.server = null;
        ServerLifecycleEvent.STOPPED.fire(server);
    }

    public static ServerChatEvent.ReceivedContext fireChatReceivedEvent(
        ServerPlayer player,
        Component message
    )
    {
        var event = new ServerChatEvent.ReceivedContext(player, message);
        ServerChatEvent.RECEIVED.fire(event);
        return event;
    }
}
