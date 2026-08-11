package io.pastework.test.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.pastework.test.common.entity.PlasmaCharge;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class PlasmaProjectileRenderer extends EntityRenderer<PlasmaCharge, PlasmaProjectileRenderState>
{
    private final PlasmaProjectileModel model;

    public PlasmaProjectileRenderer(EntityRendererProvider.Context arg) {
        super(arg);
        this.model = new PlasmaProjectileModel(arg.bakeLayer(PlasmaProjectileModel.LAYER_LOCATION));
    }

    @Override
    public void submit(
        PlasmaProjectileRenderState renderState,
        PoseStack poseStack,
        SubmitNodeCollector nodeCollector,
        CameraRenderState cameraRenderState
    )
    {
        poseStack.pushPose();
        poseStack.translate(0.125, -1.125, -0.125);
        nodeCollector
            .order(1)
            .submitModel(
                this.model,
                renderState,
                poseStack,
                RenderTypes.entityTranslucentEmissive(PlasmaProjectileModel.TEXTURE),
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                renderState.outlineColor,
                null
            );
        poseStack.popPose();

        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
    }

    @Override
    public PlasmaProjectileRenderState createRenderState()
    {
        return new PlasmaProjectileRenderState();
    }
}
