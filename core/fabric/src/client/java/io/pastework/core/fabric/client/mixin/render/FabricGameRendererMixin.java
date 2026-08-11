package io.pastework.core.fabric.client.mixin.render;

import com.llamalad7.mixinextras.sugar.Local;
import io.pastework.core.base.client.hook.ClientHooks;
import io.pastework.core.api.client.mixin.accessor.PasteworkLevelRenderAccessor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class FabricGameRendererMixin
{
    @Inject(
        method = "renderLevel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/resource/GraphicsResourceAllocator;Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V",
            shift = At.Shift.AFTER
        )
    )
    private void renderLevelAfter(
        DeltaTracker deltaTracker,
        CallbackInfo ci,
        @Local(ordinal = 1) Matrix4f matrix4f
    )
    {
        var levelRenderer = Minecraft.getInstance().levelRenderer;
        var levelRendererAccessor = ((PasteworkLevelRenderAccessor) levelRenderer);

        ClientHooks.fireAfterLevelEvent(
            levelRendererAccessor.getLevelRenderState(),
            matrix4f
        );
    }
}
