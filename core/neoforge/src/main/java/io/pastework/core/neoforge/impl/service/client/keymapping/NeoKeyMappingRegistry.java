package io.pastework.core.neoforge.impl.service.client.keymapping;

import io.pastework.core.api.client.service.keymapping.IKeyMappingRegistrar;
import io.pastework.core.base.client.impl.service.keymapping.AbstractKeyMappingRegistry;
import io.pastework.core.neoforge.impl.service.INeoEventBusDependant;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.spongepowered.asm.mixin.injection.Inject;

public final class NeoKeyMappingRegistry extends AbstractKeyMappingRegistry
    implements INeoEventBusDependant
{
    @SubscribeEvent
    private void onKeyMappingRegistration(RegisterKeyMappingsEvent event)
    {
        for(IKeyMappingRegistrar registrar : getRegistrarSet())
        {
            registrar.getKeyMappings()
                .forEach(event::register);
        }

        finalizeRegistration();
    }

    @Override
    public void handleEventBus(IEventBus eventBus)
    {
        eventBus.register(this);
    }
}
