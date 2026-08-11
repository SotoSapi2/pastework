package io.pastework.core.api.client.event.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import io.pastework.core.api.common.event.Event1;
import lombok.Getter;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.LevelRenderState;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4fc;

public interface LevelRenderEvent
{
    Event1<Context> AFTER_SKY = new Event1<>();

    Event1<Context> AFTER_OPAQUE_BLOCKS = new Event1<>();

    Event1<Context> AFTER_PARTICLES = new Event1<>();

    Event1<Context> AFTER_WEATHER = new Event1<>();

    Event1<Context> AFTER_LEVEL = new Event1<>();

    Event1<PoseStackContext> AFTER_ENTITIES = new Event1<>();

    Event1<PoseStackContext> AFTER_TRIPWIRE = new Event1<>();

    Event1<PoseStackContext> AFTER_TRANSLUCENT_BLOCKS = new Event1<>();

    @Getter
    class Context
    {
        private final DeltaTracker deltaTracker;
        private final GameRenderer gameRenderer;
        private final LevelRenderer levelRenderer;
        private final LevelRenderState levelRenderState;
        private final Camera camera;
        private final ClientLevel level;
        private final Matrix4fc viewMatrix;

        @ApiStatus.Internal
        public Context(LevelRenderState levelRenderState, Matrix4fc viewMatrix)
        {
            this.deltaTracker = Minecraft.getInstance().getDeltaTracker();
            this.gameRenderer = Minecraft.getInstance().gameRenderer;
            this.levelRenderer = Minecraft.getInstance().levelRenderer;
            this.camera = gameRenderer.getMainCamera();
            this.level = Minecraft.getInstance().level;
            this.viewMatrix = viewMatrix;
            this.levelRenderState = levelRenderState;
        }
    }

    @Getter
    class PoseStackContext extends Context
    {
        private final PoseStack poseStack;

        @ApiStatus.Internal
        public PoseStackContext(LevelRenderState levelRenderState, Matrix4fc viewMatrix, PoseStack poseStack)
        {
            super(levelRenderState, viewMatrix);
            this.poseStack = poseStack;
        }
    }
}
