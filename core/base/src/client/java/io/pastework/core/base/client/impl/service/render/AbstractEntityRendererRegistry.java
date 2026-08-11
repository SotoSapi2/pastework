package io.pastework.core.base.client.impl.service.render;

import io.pastework.core.api.client.service.render.IEntityRendererRegistry;
import io.pastework.core.api.common.service.registry.IEntryHolder;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class AbstractEntityRendererRegistry implements IEntityRendererRegistry
{
    @Getter(AccessLevel.PROTECTED)
    private final Set<RendererEntry<?>> rendererEntrySet = new HashSet<>();

    @Getter(AccessLevel.PROTECTED)
    private final Set<LayerEntry> layerEntrySet = new HashSet<>();

    private volatile boolean isRegistrationFinalized;

    @Override
    public boolean isRegistrationFinalized()
    {
        return isRegistrationFinalized;
    }

    protected void finalizeRegistration()
    {
        isRegistrationFinalized = true;
        rendererEntrySet.clear();
        layerEntrySet.clear();
    }

    @Override
    public <_TEntity extends Entity> void enqueueRenderer(
        IEntryHolder<EntityType<_TEntity>> type,
        EntityRendererProvider<_TEntity> provider
    )
    {
        var entry = new RendererEntry<>(type, provider);
        rendererEntrySet.add(entry);
    }

    @Override
    public void enqueueLayer(ModelLayerLocation location, Supplier<LayerDefinition> definition)
    {
        var entry = new LayerEntry(location, definition);
        layerEntrySet.add(entry);
    }

    protected record LayerEntry(
        ModelLayerLocation location,
        Supplier<LayerDefinition> definition
    )
    { }

    protected record RendererEntry<_TEntity extends Entity>(
        IEntryHolder<EntityType<_TEntity>> type,
        EntityRendererProvider<_TEntity> provider
    )
    { }
}
