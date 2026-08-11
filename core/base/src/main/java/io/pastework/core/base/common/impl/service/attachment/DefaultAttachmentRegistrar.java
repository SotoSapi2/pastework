package io.pastework.core.base.common.impl.service.attachment;

import io.pastework.core.api.common.service.attachment.PasteworkAttachmentType;
import io.pastework.core.api.common.service.attachment.IAttachmentRegistrar;
import io.pastework.core.api.common.service.registry.DefaultEntryHolder;
import io.pastework.core.api.common.service.registry.IEntryHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DefaultAttachmentRegistrar implements IAttachmentRegistrar
{
    private final String namespace;
    private final Set<IEntryHolder<PasteworkAttachmentType<?>>> attachmentTypeSet = new HashSet<>();

    public DefaultAttachmentRegistrar(String namespace)
    {
        this.namespace = namespace;
    }

    @Override
    public String getNamespace()
    {
        return namespace;
    }

    @Override
    public
    <_TAttachment> 
    PasteworkAttachmentType<_TAttachment> register(
        String name,
        PasteworkAttachmentType<_TAttachment> entry
    )
    {
        IEntryHolder<PasteworkAttachmentType<?>> holder = new DefaultEntryHolder<>(name, entry);
        attachmentTypeSet.add(holder);
        return entry;
    }

    @Override
    public Collection<IEntryHolder<PasteworkAttachmentType<?>>> getHolders()
    {
        return Collections.unmodifiableCollection(attachmentTypeSet);
    }
}
