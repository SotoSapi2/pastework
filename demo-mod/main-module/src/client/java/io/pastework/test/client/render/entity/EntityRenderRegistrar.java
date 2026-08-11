package io.pastework.test.client.render.entity;

import io.pastework.core.api.client.service.render.IEntityRendererRegistry;
import io.pastework.test.common.registry.ModEntities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class EntityRenderRegistrar
{
    private static final IEntityRendererRegistry RENDERER_REGISTRY = IEntityRendererRegistry.getService();

    public static void initialize()
    {
        RENDERER_REGISTRY.enqueueLayer(
            PlasmaProjectileModel.LAYER_LOCATION,
            PlasmaProjectileModel::createBodyLayer
        );

        RENDERER_REGISTRY.enqueueRenderer(
            ModEntities.BURNING_POTATO,
            ThrownItemRenderer::new
        );

        RENDERER_REGISTRY.enqueueRenderer(
            ModEntities.PLASMA_CHARGE,
            PlasmaProjectileRenderer::new
        );
    }
}
