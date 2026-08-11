package io.pastework.core.api.common.event.network;

import io.pastework.core.api.common.event.ICancellableEventContext;
import io.pastework.core.api.common.event.Event1;
import io.pastework.core.api.common.event.AbstractEventContext;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ServerChatEvent
{
    /**
     * Fired when the server receives a chat message from a player.
     * <p>
     * This event can be cancelled to stop broadcasting to every player.
     */
    Event1<ReceivedContext> RECEIVED = new Event1<>();

    @Getter
    final class ReceivedContext extends AbstractEventContext implements ICancellableEventContext
    {
        @Setter
        private boolean cancelled;

        @Setter
        private Component message;

        private final Component originalMessage;
        private final ServerPlayer player;

        public boolean isModified()
        {
            return originalMessage != message;
        }

        @ApiStatus.Internal
        public ReceivedContext(ServerPlayer player, Component message)
        {
            this.player = player;
            this.originalMessage = message;
            this.message = message;
        }
    }
}
