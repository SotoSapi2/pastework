package io.pastework.core.api.client.event.input;

import io.pastework.core.api.common.event.Event1;
import io.pastework.core.api.common.event.ICancellableEventContext;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonInfo;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector2d;

@ApiStatus.NonExtendable
public interface ClientInputEvent
{
    Event1<KeyPressedContext> KEY_PRESSED = new Event1<>();

    Event1<MouseMoveEventContext> MOUSE_MOVED = new Event1<>();

    Event1<MousePressContext> MOUSE_BUTTON_PRESSED = new Event1<>();

    Event1<MouseScrollContext> MOUSE_SCROLLED = new Event1<>();

    @Getter
    abstract class AbstractContext implements ICancellableEventContext
    {
        @Setter
        private boolean isCancelled;
        private final long window;

        protected AbstractContext(long window)
        {
            this.window = window;
        }
    }

    @Getter
    final class KeyPressedContext extends AbstractContext
    {
        private final KeyEvent keyEvent;
        private final int action;

        @ApiStatus.Internal
        public KeyPressedContext(long window, KeyEvent keyEvent, int action)
        {
            super(window);
            this.keyEvent = keyEvent;
            this.action = action;
        }
    }

    @Getter
    final class MouseMoveEventContext extends AbstractContext
    {
        private final MouseHandler mouseHandler;
        private final Vector2d pos;
        private final Vector2d delta;

        @ApiStatus.Internal
        public MouseMoveEventContext(long window, MouseHandler mouseHandler, Vector2d pos, Vector2d delta)
        {
            super(window);
            this.mouseHandler = mouseHandler;
            this.pos = pos;
            this.delta = delta;
        }
    }

    @Getter
    final class MousePressContext extends AbstractContext
    {
        private final MouseHandler mouseHandler;
        private final MouseButtonInfo buttonInfo;
        private final int action;

        @ApiStatus.Internal
        public MousePressContext(long window, MouseHandler mouseHandler, MouseButtonInfo buttonInfo, int action)
        {
            super(window);
            this.mouseHandler = mouseHandler;
            this.buttonInfo = buttonInfo;
            this.action = action;
        }
    }

    @Getter
    final class MouseScrollContext extends AbstractContext
    {
        private final MouseHandler mouseHandler;
        private final Vector2d delta;

        public MouseScrollContext(long window, MouseHandler mouseHandler, Vector2d delta)
        {
            super(window);
            this.mouseHandler = mouseHandler;
            this.delta = delta;
        }
    }
}
