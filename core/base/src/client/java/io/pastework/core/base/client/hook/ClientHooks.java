package io.pastework.core.base.client.hook;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import io.pastework.core.api.client.event.ClientTickEvent;
import io.pastework.core.api.client.event.input.ClientInputEvent;
import io.pastework.core.api.client.event.network.ClientChatEvent;
import io.pastework.core.api.client.event.rendering.LevelRenderEvent;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;
import org.joml.Vector2d;

@UtilityClass
@ApiStatus.Internal
public final class ClientHooks
{
    @Getter
    private static final MouseHandler mouseHandler = Minecraft.getInstance().mouseHandler;

    @Getter
    @Nullable
    private static final Window windowHandle = Minecraft.getInstance()
        .getWindow();

    @Getter
    private static Vector2d lastMousePos = new Vector2d(
        0,
        0
    );

    public static void fireClientPreTickEvent(Minecraft minecraft)
    {
        ClientTickEvent.PRE.fire(minecraft);
    }

    public static void fireClientPostTickEvent(Minecraft minecraft)
    {
        ClientTickEvent.POST.fire(minecraft);
    }

    public static ClientInputEvent.KeyPressedContext fireKeyboardPressEvent(
        long window,
        KeyEvent keyEvent,
        int action
    )
    {
        var ctx = new ClientInputEvent.KeyPressedContext(
            window,
            keyEvent,
            action
        );

        ClientInputEvent.KEY_PRESSED.fire(ctx);
        return ctx;
    }

    public static ClientInputEvent.MouseMoveEventContext fireMouseMoveEvent(
        long window,
        double posX,
        double posY
    )
    {
        var pos = new Vector2d(posX, posY);

        if (lastMousePos.equals(0, 0))
        {
            lastMousePos = pos;
        }

        var delta = pos.sub(lastMousePos);
        var ctx = new ClientInputEvent.MouseMoveEventContext(
            window,
            mouseHandler,
            pos,
            delta
        );

        ClientInputEvent.MOUSE_MOVED.fire(ctx);
        lastMousePos = pos;

        return ctx;
    }

    public static ClientInputEvent.MousePressContext fireMousePressEvent(
        long window,
        MouseButtonInfo buttonInfo,
        int action
    )
    {
        var ctx = new ClientInputEvent.MousePressContext(
            window,
            mouseHandler,
            buttonInfo,
            action
        );

        ClientInputEvent.MOUSE_BUTTON_PRESSED.fire(ctx);
        return ctx;
    }

    public static ClientInputEvent.MouseScrollContext fireMouseScrollEvent(
        long window,
        double deltaX,
        double deltaY
    )
    {
        var ctx = new ClientInputEvent.MouseScrollContext(
            window,
            mouseHandler,
            new Vector2d(
                deltaX,
                deltaY
            )
        );

        ClientInputEvent.MOUSE_SCROLLED.fire(ctx);
        return ctx;
    }

    public static ClientChatEvent.ReceivedContext fireChatReceivedEvent(Component component)
    {
        var ctx = new ClientChatEvent.ReceivedContext(component);

        ClientChatEvent.RECEIVED.fire(ctx);
        return ctx;
    }

    public static ClientChatEvent.DisplayedContext fireChatDisplayedEvent(Component component)
    {
        var ctx = new ClientChatEvent.DisplayedContext(component);

        ClientChatEvent.DISPLAYED.fire(ctx);
        return ctx;
    }

    public static ClientChatEvent.SendContext fireChatSendEvent(String message)
    {
        var ctx = new ClientChatEvent.SendContext(message);

        ClientChatEvent.SEND.fire(ctx);
        return ctx;
    }

    public static void fireAfterSkyEvent(
        LevelRenderState levelRenderState,
        Matrix4fc viewMatrix
    )
    {
        var ctx = new LevelRenderEvent.Context(levelRenderState, viewMatrix);
        LevelRenderEvent.AFTER_SKY.fire(ctx);
    }

    public static void fireAfterOpaqueBlocksEvent(
        LevelRenderState levelRenderState,
        Matrix4fc viewMatrix
    )
    {
        var ctx = new LevelRenderEvent.Context(levelRenderState, viewMatrix);
        LevelRenderEvent.AFTER_OPAQUE_BLOCKS.fire(ctx);
    }

    public static void fireAfterEntitiesEvent(
        LevelRenderState levelRenderState,
        Matrix4fc viewMatrix,
        PoseStack poseStack
    )
    {
        var ctx = new LevelRenderEvent.PoseStackContext(levelRenderState, viewMatrix, poseStack);
        LevelRenderEvent.AFTER_ENTITIES.fire(ctx);
    }

    public static void fireAfterTranslucentBlocksEvent(
        LevelRenderState levelRenderState,
        Matrix4fc viewMatrix,
        PoseStack poseStack
    )
    {
        var ctx = new LevelRenderEvent.PoseStackContext(levelRenderState, viewMatrix, poseStack);
        LevelRenderEvent.AFTER_TRANSLUCENT_BLOCKS.fire(ctx);
    }

    public static void fireAfterTripwireBlocksEvent(
        LevelRenderState levelRenderState,
        Matrix4fc viewMatrix,
        PoseStack poseStack
    )
    {
        var ctx = new LevelRenderEvent.PoseStackContext(levelRenderState, viewMatrix, poseStack);
        LevelRenderEvent.AFTER_TRIPWIRE.fire(ctx);
    }

    public static void fireAfterWeatherEvent(
        LevelRenderState levelRenderState,
        Matrix4fc viewMatrix
    )
    {
        var ctx = new LevelRenderEvent.Context(levelRenderState, viewMatrix);
        LevelRenderEvent.AFTER_WEATHER.fire(ctx);
    }

    public static void fireAfterParticlesEvent(
        LevelRenderState levelRenderState,
        Matrix4fc viewMatrix
    )
    {
        var ctx = new LevelRenderEvent.Context(levelRenderState, viewMatrix);
        LevelRenderEvent.AFTER_PARTICLES.fire(ctx);
    }

    public static void fireAfterLevelEvent(
        LevelRenderState levelRenderState,
        Matrix4fc viewMatrix
    )
    {
        var ctx = new LevelRenderEvent.Context(levelRenderState, viewMatrix);
        LevelRenderEvent.AFTER_LEVEL.fire(ctx);
    }
}
