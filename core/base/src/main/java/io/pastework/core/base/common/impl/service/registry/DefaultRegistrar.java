package io.pastework.core.base.common.impl.service.registry;

import io.pastework.core.api.common.service.registry.ICommonRegistry;
import io.pastework.core.api.common.service.registry.LazyEntryHolder;
import io.pastework.core.api.common.service.registry.ICommonRegistrar;
import io.pastework.core.api.exception.RegistryException;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DefaultRegistrar<TBaseEntry> implements ICommonRegistrar<TBaseEntry>
{
    private final ICommonRegistry registry;
    private final String namespace;
    private final ResourceKey<? extends Registry<TBaseEntry>> registryKey;

    @Getter(AccessLevel.PROTECTED)
    private final Set<LazyEntryHolder<? extends TBaseEntry>> holderSet = new HashSet<>();

    public DefaultRegistrar(
        ICommonRegistry registry,
        String namespace,
        ResourceKey<? extends Registry<TBaseEntry>> registryKey
    )
    {
        this.registry = registry;
        this.namespace = namespace;
        this.registryKey = registryKey;
    }

    @Override
    public ResourceKey<? extends Registry<TBaseEntry>> getRegistryKey()
    {
        return registryKey;
    }

    @Override
    public boolean isRegistrationFinalized()
    {
        return registry.isRegistrationFinalized();
    }

    @Override
    public String getNamespace()
    {
        return namespace;
    }

    @Override
    public
    <_TEntry extends TBaseEntry>
    LazyEntryHolder<_TEntry> register(LazyEntryHolder<_TEntry> entryHolder)
    {
        if(registry.isRegistrationFinalized())
        {
            throw new RegistryException(
                "Cannot register new entry after registration is finalized."
            );
        }

        holderSet.add(entryHolder);
        return entryHolder;
    }

    @Override
    public Collection<LazyEntryHolder<? extends TBaseEntry>> getEntries()
    {
        return Collections.unmodifiableCollection(holderSet);
    }
}