package io.pastework.core.base.client.mixin.input;

import io.pastework.core.base.client.hook.ClientHooks;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class ClientMouseHandlerMixin
{
    @Inject(method = "onMove", at = @At("HEAD"), cancellable = true)
    private void onMove(long window, double x, double y, CallbackInfo ci)
    {
        if(ClientHooks.fireMouseMoveEvent(window, x, y).isCancelled())
        {
            ci.cancel();
        }
    }

    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    private void onPress(long window, MouseButtonInfo buttonInfo, int action, CallbackInfo ci)
    {
        if(ClientHooks.fireMousePressEvent(window, buttonInfo, action).isCancelled())
        {
            ci.cancel();
        }
    }

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void onScroll(long window, double xOffset, double yOffset, CallbackInfo ci)
    {
        if(ClientHooks.fireMouseScrollEvent(window, xOffset, yOffset).isCancelled())
        {
            ci.cancel();
        }
    }
}
