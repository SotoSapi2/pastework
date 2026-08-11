package io.pastework.core.api.client.mixin.accessor;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.LevelRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LevelRenderer.class)
public interface PasteworkLevelRenderAccessor
{
    @Accessor
    LevelRenderState getLevelRenderState();
}
