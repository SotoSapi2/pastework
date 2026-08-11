package io.pastework.core.api.client.event.network;

import io.pastework.core.api.common.event.Event1;
import io.pastework.core.api.common.event.ICancellableEventContext;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

public interface ClientChatEvent
{
    Event1<SendContext> SEND = new Event1<>();

    Event1<ReceivedContext> RECEIVED = new Event1<>();

    Event1<DisplayedContext> DISPLAYED = new Event1<>();

    abstract class AbstractContext implements ICancellableEventContext {
        @Getter
        @Setter
        private boolean isCancelled;

        public abstract boolean isMessageModified();
    }

    final class SendContext extends AbstractContext {
        @Getter
        private final String originalMessage;

        @Getter
        @Setter
        private String message;

        @Override
        public boolean isMessageModified() {
            return originalMessage.equals(message);
        }

        @ApiStatus.Internal
        public SendContext(String message) {
            this.originalMessage = message;
        }
    }

    @Getter
    final class ReceivedContext extends AbstractContext {
        @Getter
        private final Component originalMessage;

        @Setter
        private Component message;

        @Override
        public boolean isMessageModified() {
            return originalMessage != message;
        }

        @ApiStatus.Internal
        public ReceivedContext(Component message) {
            this.originalMessage = message;
            this.message = message;
        }
    }

    @Getter
    final class DisplayedContext extends AbstractContext {
        @Getter
        private final Component originalMessage;

        @Setter
        private Component message;

        @Override
        public boolean isMessageModified() {
            return originalMessage != message;
        }

        public DisplayedContext(Component message) {
            this.originalMessage = message;
            this.message = message;
        }
    }
}
