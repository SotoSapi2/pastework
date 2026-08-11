package io.pastework.core.base.client.impl.service.keymapping;

import io.pastework.core.api.client.service.keymapping.IKeyMappingRegistrar;
import io.pastework.core.api.client.service.keymapping.IKeyMappingRegistry;
import io.pastework.core.api.exception.RegistryException;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class AbstractKeyMappingRegistry implements IKeyMappingRegistry
{
    @Getter(AccessLevel.PROTECTED)
    private final Set<IKeyMappingRegistrar> registrarSet = new HashSet<>();

    private volatile boolean isRegistrationFinalized;

    @Override
    public boolean isRegistrationFinalized()
    {
        return isRegistrationFinalized;
    }

    protected void finalizeRegistration()
    {
        isRegistrationFinalized = true;
        registrarSet.clear();
    }

    @Override
    public IKeyMappingRegistrar createSet()
    {
        return new DefaultKeyMappingRegistrar();
    }

    @Override
    public void enqueueRegistrar(IKeyMappingRegistrar holder)
    {
        if(isRegistrationFinalized)
        {
            throw new RegistryException(
                "Cannot enqueue registry holder after registration has been finalized."
            );
        }

        synchronized (registrarSet)
        {
            registrarSet.add(holder);
        }
    }
}
