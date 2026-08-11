package io.pastework.core.base.common.impl.service.registry;

import io.pastework.core.api.common.service.registry.ICommonRegistrar;
import io.pastework.core.api.common.service.registry.ICommonRegistry;
import io.pastework.core.api.exception.RegistryException;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractCommonRegistry implements ICommonRegistry
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommonRegistry.class);

    // regis key -> enqueued new native regis
    @Getter(AccessLevel.PROTECTED)
    private final Map<ResourceKey<?>, Registry<?>> newRegistryMap = new HashMap<>();

    // regis key -> holder set
    @Getter(AccessLevel.PROTECTED)
    private final Map<ResourceKey<?>, Set<ICommonRegistrar<?>>> registrarMap = new HashMap<>();

    private volatile boolean isRegistrationFinalized = false;

    @Override
    public boolean isRegistrationFinalized()
    {
        return isRegistrationFinalized;
    }

    protected void finalizeRegistration()
    {
        isRegistrationFinalized = true;
        newRegistryMap.clear();
        registrarMap.clear();
    }

    @Override
    public <T> ICommonRegistrar<T> createRegistrar(String namespace, ResourceKey<? extends Registry<T>> registryKey)
    {
        return new DefaultRegistrar<>(
            this,
            namespace,
            registryKey
        );
    }

    @Override
    public <T> void enqueueRegistrar(ICommonRegistrar<T> holder)
    {
        if (isRegistrationFinalized)
        {
            throw new RegistryException(
                "Cannot enqueue registrar after registration has been finalized."
            );
        }

        synchronized (registrarMap)
        {
            registrarMap.computeIfAbsent(holder.getRegistryKey(), it -> new HashSet<>())
                .add(holder);

            LOGGER.info("New registrar enqueued: {}", holder.getRegistryKey());
        }
    }

    @Override
    public <T> void enqueueNewRegistry(ResourceKey<? extends Registry<T>> key, boolean synced)
    {
        if (isRegistrationFinalized)
        {
            throw new RegistryException(
                "Cannot enqueue new registry after registration has been finalized."
            );
        }

        throwIfRegistryEnqueued(key);
        Registry<T> createdRegistry = newRegistry(key, null, synced);
        enqueueNewRegistryInstance(key, createdRegistry);
    }

    @Override
    public <T> void enqueueNewRegistryDefaulted(
        ResourceKey<? extends Registry<T>> key,
        Identifier defaultEntryKey,
        boolean synced
    )
    {
        if (isRegistrationFinalized)
        {
            throw new RegistryException(
                "Cannot register new registry after registration has been finalized."
            );
        }

        throwIfRegistryEnqueued(key);
        Registry<T> createdRegistry = newRegistry(key, defaultEntryKey, synced);
        enqueueNewRegistryInstance(key, createdRegistry);
    }

    public
    <T> void enqueueNewRegistryInstance(
        ResourceKey<? extends Registry<T>> key,
        Registry<T> registry
    ) throws RegistryException
    {
        if (isRegistrationFinalized)
        {
            throw new RegistryException(
                "Cannot register new registry after registration has been finalized."
            );
        }

        synchronized (newRegistryMap)
        {
            throwIfRegistryEnqueued(key);
            newRegistryMap.put(key, registry);

            LOGGER.info("New registry enqueued: {}", key);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Holder.Reference<? extends Registry<T>> requestNativeRegistry(ResourceKey<? extends Registry<T>> key)
    {
        return (Holder.Reference<? extends Registry<T>>) BuiltInRegistries.REGISTRY.get(key.identifier())
            .orElseThrow(() ->
            {
                return new RegistryException(String.format(
                    "Couldn't find native registry with key '%s' or it hasn't been initialized.",
                    key.identifier()
                ));
            });
    }

    private void throwIfRegistryEnqueued(ResourceKey<?> key)
    {
        synchronized (newRegistryMap)
        {
            if (newRegistryMap.containsKey(key))
            {
                throw new RegistryException(String.format(
                    "Registry with %s key is already enqueued.",
                    key.identifier()
                ));
            }
        }
    }
    
    protected abstract <T> Registry<T> newRegistry(
        ResourceKey<? extends Registry<T>> key,
        @Nullable Identifier defaultElementKey,
        boolean synced
    );
}
