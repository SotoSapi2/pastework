package io.pastework.core.fabric.client.mixin.render;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.pastework.core.base.client.hook.ClientHooks;
import io.pastework.core.api.client.mixin.accessor.PasteworkLevelRenderAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.LevelRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class FabricLevelRenderMixin
{
    @Shadow
    @Final
    private LevelRenderState levelRenderState;

    @Inject(
        method = "method_62216",
        at = @At("RETURN")
    )
    private void onAfterWeather(CallbackInfo ci)
    {
        ClientHooks.fireAfterWeatherEvent(levelRenderState, RenderSystem.getModelViewMatrix());
    }

    @Inject(
        method = "method_62213",
        at = @At("RETURN")
    )
    private void onAfterParticles(CallbackInfo ci)
    {
        ClientHooks.fireAfterParticlesEvent(levelRenderState, RenderSystem.getModelViewMatrix());
    }

    @Inject(
        method = "method_62215",
        at = @At("RETURN")
    )
    private static void onAfterSky(CallbackInfo ci)
    {
        var levelRenderer = Minecraft.getInstance().levelRenderer;
        var levelRendererAccessor = ((PasteworkLevelRenderAccessor) levelRenderer);

        ClientHooks.fireAfterSkyEvent(
            levelRendererAccessor.getLevelRenderState(),
            RenderSystem.getModelViewMatrix()
        );
    }

    @Inject(
        method = "method_62214",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;renderGroup(Lnet/minecraft/client/renderer/chunk/ChunkSectionLayerGroup;Lcom/mojang/blaze3d/textures/GpuSampler;)V",
            ordinal = 0,
            shift = At.Shift.AFTER
        )
    )
    private void onAfterOpaqueBlocks(CallbackInfo ci)
    {
        ClientHooks.fireAfterOpaqueBlocksEvent(levelRenderState, RenderSystem.getModelViewMatrix());
    }

    @Inject(
        method = "method_62214",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;renderGroup(Lnet/minecraft/client/renderer/chunk/ChunkSectionLayerGroup;Lcom/mojang/blaze3d/textures/GpuSampler;)V",
            ordinal = 1,
            shift = At.Shift.AFTER
        )
    )
    private void onAfterTranslucentBlocks(
        CallbackInfo ci,
        @Local PoseStack poseStack
    )
    {
        ClientHooks.fireAfterOpaqueBlocksEvent(levelRenderState, RenderSystem.getModelViewMatrix());
    }

    @Inject(
        method = "method_62214",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/chunk/ChunkSectionsToRender;renderGroup(Lnet/minecraft/client/renderer/chunk/ChunkSectionLayerGroup;Lcom/mojang/blaze3d/textures/GpuSampler;)V",
            ordinal = 2,
            shift = At.Shift.AFTER
        )
    )
    private void onAfterTripwireBlocks(
        CallbackInfo ci,
        @Local PoseStack poseStack
    )
    {
        ClientHooks.fireAfterTripwireBlocksEvent(levelRenderState, RenderSystem.getModelViewMatrix(), poseStack);
    }

    @Inject(
        method = "method_62214",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/OutlineBufferSource;endOutlineBatch()V",
            shift = At.Shift.AFTER
        )
    )
    private void onAfterEntities(
        CallbackInfo ci,
        @Local PoseStack poseStack
    )
    {
        ClientHooks.fireAfterEntitiesEvent(levelRenderState, RenderSystem.getModelViewMatrix(), poseStack);
    }
}
