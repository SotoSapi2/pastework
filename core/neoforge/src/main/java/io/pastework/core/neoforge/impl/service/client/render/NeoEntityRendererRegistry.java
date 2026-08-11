package io.pastework.core.neoforge.impl.service.client.render;

import io.pastework.core.base.client.impl.service.render.AbstractEntityRendererRegistry;
import io.pastework.core.neoforge.impl.service.INeoEventBusDependant;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class NeoEntityRendererRegistry extends AbstractEntityRendererRegistry implements INeoEventBusDependant
{
    @SubscribeEvent
    private void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        for (RendererEntry<?> entry : getRendererEntrySet())
        {
            register(event, entry);
        }
    }

    @SubscribeEvent
    private void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        for (LayerEntry entry : getLayerEntrySet())
        {
            event.registerLayerDefinition(entry.location(), entry.definition());
        }
    }

    @SubscribeEvent
    private void onClientSetup(FMLClientSetupEvent event)
    {
        finalizeRegistration();
    }

    private <_TEntity extends Entity> void register(
        EntityRenderersEvent.RegisterRenderers event,
        RendererEntry<_TEntity> entry
    )
    {
        event.registerEntityRenderer(
            entry.type().getEntry(),
            entry.provider()
        );
    }

    @Override
    public void handleEventBus(IEventBus eventBus)
    {
        eventBus.register(this);
    }
}
