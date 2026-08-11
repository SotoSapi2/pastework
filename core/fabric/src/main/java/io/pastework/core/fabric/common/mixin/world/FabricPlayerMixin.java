package io.pastework.core.fabric.common.mixin.world;

import io.pastework.core.base.common.hook.LevelHooks;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class FabricPlayerMixin
{
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick_head(CallbackInfo ci)
    {
        LevelHooks.firePlayerPostTickEvent((Player) (Object) this);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick_tail(CallbackInfo ci)
    {
        LevelHooks.firePlayerPreTickEvent((Player) (Object) this);
    }
}
