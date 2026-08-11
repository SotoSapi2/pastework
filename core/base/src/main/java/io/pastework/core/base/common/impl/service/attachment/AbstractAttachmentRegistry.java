package io.pastework.core.base.common.impl.service.attachment;

import io.pastework.core.api.common.service.attachment.PasteworkAttachmentType;
import io.pastework.core.api.common.service.attachment.IAttachmentRegistrar;
import io.pastework.core.api.common.service.attachment.IAttachmentRegistry;
import io.pastework.core.api.exception.RegistryException;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractAttachmentRegistry<T> implements IAttachmentRegistry
{
    @Getter(AccessLevel.PROTECTED)
    private final Map<PasteworkAttachmentType<?>, T> attachmentRegistryMap = new HashMap<>();

    @Getter(AccessLevel.PROTECTED)
    private final Set<IAttachmentRegistrar> registrarSet = new HashSet<>();

    private volatile boolean isRegistrationFinalized;

    @Override
    public boolean isRegistrationFinalized()
    {
        return isRegistrationFinalized;
    }

    @Override
    public IAttachmentRegistrar createRegistrySet(String namespace)
    {
        return new DefaultAttachmentRegistrar(namespace);
    }

    @Override
    public void enqueueRegistrar(IAttachmentRegistrar holder)
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

    protected void finalizeRegistration()
    {
        for (var registrar : registrarSet)
        {
            var namespace = registrar.getNamespace();

            for(var holder : registrar.getHolders())
            {
                var id = holder.createIdentifier(namespace);
                registerAttachment(
                    id,
                    holder.getEntry()
                );
            }
        }

        isRegistrationFinalized = true;
        registrarSet.clear();
    }

    protected abstract <V> void registerAttachment(Identifier key, PasteworkAttachmentType<V> type);
}
