package io.pastework.core.neoforge;

import io.pastework.core.api.Pastework;
import io.pastework.core.api.common.service.registry.ICommonRegistry;
import io.pastework.core.base.common.hook.BootstrapHooks;
import io.pastework.core.base.common.impl.internal.PasteworkImpl;
import io.pastework.core.neoforge.impl.service.INeoEventBusDependant;
import io.pastework.core.neoforge.impl.service.common.registry.NeoCommonRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.jetbrains.annotations.ApiStatus;

@Mod(NeoPasteworkEntrypoint.MOD_ID)
public final class NeoPasteworkEntrypoint
{
    public static final String MOD_ID = "pastework_core_neoforge";

    @ApiStatus.Internal
    public NeoPasteworkEntrypoint(IEventBus eventBus)
    {
        final PasteworkImpl pastework = (PasteworkImpl) Pastework.INSTANCE;

        if (pastework.isInitialized())
        {
            throw new IllegalStateException("Shouldn't be constructed after initialization.");
        }

        pastework.prepareServices();

        for(var service : pastework.getServices())
        {
            // DO NOT register event directly here and pass the event bus to the registrator
            // because it will fuck up the class loading.
            // And I have no idea what's causing it and this thing somehow solve it?
            if(service instanceof INeoEventBusDependant neoEventBusDependant)
            {
                neoEventBusDependant.handleEventBus(eventBus);
            }
        }

        pastework.invokeEveryDependantEntrypoint();

        eventBus.addListener(this::onCommonSetup);
        eventBus.addListener(this::onClientSetup);
        eventBus.addListener(this::onServerSetup);
        eventBus.addListener(this::onLoadComplete);
    }

    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        BootstrapHooks.fireCommonSetupEvent();
    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        BootstrapHooks.fireClientSetupEvent();
    }

    private void onServerSetup(FMLDedicatedServerSetupEvent event)
    {
        BootstrapHooks.fireServerSetupEvent();
    }

    private void onLoadComplete(FMLLoadCompleteEvent event)
    {
        BootstrapHooks.fireLoadingCompleteEvent();
    }
}
