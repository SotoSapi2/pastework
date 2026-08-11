package io.pastework.core.neoforge.impl.service.common.registry;

import io.pastework.core.api.common.service.registry.ICommonRegistrar;
import io.pastework.core.base.common.impl.service.registry.AbstractCommonRegistry;
import io.pastework.core.neoforge.impl.service.INeoEventBusDependant;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class NeoCommonRegistry extends AbstractCommonRegistry implements INeoEventBusDependant
{
    @Override
    protected <T> Registry<T> newRegistry(
        ResourceKey<? extends Registry<T>> key,
        @Nullable Identifier defaultElementKey,
        boolean synced
    )
    {
        var registryBuilder = new RegistryBuilder<>(key)
            .sync(synced);

        if (defaultElementKey != null)
        {
            registryBuilder.defaultKey(defaultElementKey);
        }

        return registryBuilder.create();
    }

    @SuppressWarnings("unchecked")
    private void processHolderRegistration(RegisterEvent event, Set<ICommonRegistrar<?>> holders)
    {
        for (var holder : holders)
        {
            if (holder.getEntries().isEmpty())
            {
                return;
            }

            String namespace = holder.getNamespace();

            var registryKey = (ResourceKey<? extends Registry<Object>>) holder
                .getRegistryKey();

            event.register(registryKey, regisHelper ->
            {
                for (var regisEntry : holder.getEntries())
                {
                    regisHelper.register(
                        regisEntry.createIdentifier(namespace),
                        regisEntry.getEntry()
                    );
                }
            });
        }
    }

    @SubscribeEvent
    private void onNewRegistryInit(NewRegistryEvent event)
    {
        for (var register : getNewRegistryMap().values())
        {
            event.register(register);
        }
    }

    @SubscribeEvent
    private void onRegistrationInit(RegisterEvent event)
    {
        var regisKey = event.getRegistryKey();

        if(!getRegistrarMap().containsKey(regisKey))
        {
            return;
        }

        processHolderRegistration(event, getRegistrarMap().get(regisKey));
    }

    @SubscribeEvent
    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        finalizeRegistration();
    }

    @Override
    public void handleEventBus(IEventBus eventBus)
    {
        eventBus.register(this);
    }
}
