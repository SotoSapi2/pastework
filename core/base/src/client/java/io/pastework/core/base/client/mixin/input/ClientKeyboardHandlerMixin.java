package io.pastework.core.base.client.mixin.input;

import io.pastework.core.base.client.hook.ClientHooks;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class ClientKeyboardHandlerMixin
{
    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void onKey(long window, int action, KeyEvent event, CallbackInfo ci)
    {
        if(ClientHooks.fireKeyboardPressEvent(window, event, action).isCancelled())
        {
            ci.cancel();
        }
    }
}
