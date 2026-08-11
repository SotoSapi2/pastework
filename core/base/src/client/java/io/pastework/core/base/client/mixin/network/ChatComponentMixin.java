package io.pastework.core.base.client.mixin.network;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.pastework.core.base.client.hook.ClientHooks;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public class ChatComponentMixin
{
    @Inject(
        method = "addMessage(Lnet/minecraft/network/chat/Component;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void addMessage(
        Component chatComponent,
        CallbackInfo ci,
        @Local(ordinal = 0, argsOnly = true) LocalRef<Component> componentRef
    )
    {
        var event = ClientHooks.fireChatDisplayedEvent(chatComponent);

        if(event.isCancelled())
        {
            ci.cancel();
        }
        else if(event.isMessageModified())
        {
            componentRef.set(event.getMessage());
        }
    }
}
