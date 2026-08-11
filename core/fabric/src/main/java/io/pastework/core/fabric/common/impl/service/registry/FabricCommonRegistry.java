package io.pastework.core.fabric.common.impl.service.registry;

import com.mojang.serialization.Lifecycle;
import io.pastework.core.api.common.service.registry.ICommonRegistrar;
import io.pastework.core.base.common.impl.service.registry.AbstractCommonRegistry;
import io.pastework.core.fabric.common.impl.service.IFabricRegistrable;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

public class FabricCommonRegistry extends AbstractCommonRegistry implements IFabricRegistrable
{
    @Override
    protected <T> Registry<T> newRegistry(
        ResourceKey<? extends Registry<T>> key,
        @Nullable Identifier defaultElementKey,
        boolean synced
    )
    {
        var rawRegistry = defaultElementKey != null ?
                          new DefaultedMappedRegistry<>(defaultElementKey.toString(), key, Lifecycle.stable(), false) :
                          new MappedRegistry<>(key, Lifecycle.stable(), false);

        var builder = FabricRegistryBuilder.from(rawRegistry);

        if (synced)
        {
            builder.attribute(RegistryAttribute.SYNCED);
        }

        return builder.buildAndRegister();
    }

    private <T> void registerRegistrar(Registry<T> registry, ICommonRegistrar<T> holder)
    {
        var namespace = holder.getNamespace();

        for (var entry : holder.getEntries())
        {
            Registry.register(
                registry,
                entry.createIdentifier(namespace),
                entry.getEntry()
            );
        }
    }

    @Override
    public void processRegistration()
    {
        if(isRegistrationFinalized())
        {
            throw new IllegalStateException();
        }

        for (var entry : getRegistrarMap().entrySet())
        {
            var registryKey = (ResourceKey<? extends Registry<Object>>) entry.getKey();
            var holders = entry.getValue();
            var registry = requestNativeRegistry(registryKey);

            for (var holder : holders)
            {
                registerRegistrar(registry.value(), (ICommonRegistrar<Object>) holder);
            }
        }

        finalizeRegistration();
    }
}
