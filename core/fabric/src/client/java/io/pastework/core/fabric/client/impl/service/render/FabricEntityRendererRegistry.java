package io.pastework.core.fabric.client.impl.service.render;

import io.pastework.core.base.client.impl.service.render.AbstractEntityRendererRegistry;
import io.pastework.core.fabric.common.impl.service.IFabricRegistrable;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;

public final class FabricEntityRendererRegistry extends AbstractEntityRendererRegistry implements
    IFabricRegistrable
{
    @Override
    public boolean isRegistrationFinalized()
    {
        return super.isRegistrationFinalized();
    }

    @Override
    public void processRegistration()
    {
        if (isRegistrationFinalized())
        {
            throw new IllegalStateException();
        }

        for(RendererEntry<?> entry : getRendererEntrySet())
        {
            registerRenderer(entry);
        }

        for (LayerEntry entry : getLayerEntrySet())
        {
            EntityModelLayerRegistry.registerModelLayer(
                entry.location(),
                () -> entry.definition().get()
            );
        }

        finalizeRegistration();
    }

    private
    <_TEntity extends Entity>
    void registerRenderer(RendererEntry<_TEntity> entry)
    {
        EntityRenderers.register(
            entry.type().getEntry(),
            entry.provider()
        );
    }
}
