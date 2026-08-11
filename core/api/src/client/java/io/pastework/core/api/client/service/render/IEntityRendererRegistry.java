package io.pastework.core.api.client.service.render;

import io.pastework.core.api.Pastework;
import io.pastework.core.api.common.service.registry.IEntryHolder;
import io.pastework.spi.IPasteworkService;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public interface IEntityRendererRegistry extends IPasteworkService
{
    static IEntityRendererRegistry getService()
    {
        return Pastework.INSTANCE.getService(IEntityRendererRegistry.class);
    }

    boolean isRegistrationFinalized();

    <_TEntity extends Entity>
    void enqueueRenderer(
        IEntryHolder<EntityType<_TEntity>> type,
        EntityRendererProvider<_TEntity> provider
    );

    void enqueueLayer(
        ModelLayerLocation location,
        Supplier<LayerDefinition> definition
    );
}
