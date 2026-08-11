package io.pastework.core.neoforge.impl.service.common.attachment;

import io.pastework.core.api.Pastework;
import io.pastework.core.api.common.service.attachment.PasteworkAttachmentType;
import io.pastework.core.api.common.service.attachment.IAttachableExtension;
import io.pastework.core.api.common.service.attachment.IAttachmentRegistry;
import io.pastework.core.api.exception.RegistryException;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface INeoAttachableExtension extends IAttachableExtension
{
    IAttachmentHolder getAttachmentHolder();

    default NeoAttachmentRegistry getAttachmentRegistry()
    {
        IAttachmentRegistry attachmentRegistry = Pastework.INSTANCE.getService(IAttachmentRegistry.class);

        if(attachmentRegistry instanceof NeoAttachmentRegistry neoAttachmentRegistry)
        {
            return neoAttachmentRegistry;
        }

        throw new IllegalStateException("IAttachmentRegistry service must be an instance of NeoAttachmentRegistry.");
    }

    @Override
    default <_TValue> _TValue emplaceAttachment(PasteworkAttachmentType<_TValue> attachment)
        throws RegistryException, WrongThreadException
    {
        IAttachmentHolder thiz = getAttachmentHolder();
        var neoAttachmentType = getAttachmentRegistry().getNeoAttachmentType(attachment);
        var value = attachment.getInitializer().get();

        thiz.setData(neoAttachmentType, value);
        return value;
    }

    @Override
    default <_TValue> void setAttachment(PasteworkAttachmentType<_TValue> attachment, _TValue value)
        throws RegistryException, WrongThreadException
    {
        IAttachmentHolder thiz = getAttachmentHolder();
        var neoAttachmentType = getAttachmentRegistry().getNeoAttachmentType(attachment);

        thiz.setData(neoAttachmentType, value);
    }

    @Override
    default <_TValue> _TValue editAttachment(
        PasteworkAttachmentType<_TValue> attachment,
        IAttachableExtension.ModifierCallback<_TValue> modifier
    ) throws NoSuchElementException, RegistryException
    {
        IAttachmentHolder thiz = getAttachmentHolder();
        var neoAttachmentType = getAttachmentRegistry().getNeoAttachmentType(attachment);

        if(!thiz.hasData(neoAttachmentType))
        {
            throw new NoSuchElementException("Holder doesn't have the passed attachment.");
        }

        var origValue = thiz.getData(neoAttachmentType);
        var newValue = modifier.invoke(origValue);

        thiz.setData(neoAttachmentType, newValue);
        return newValue;
    }

    @Override
    default <_TValue> Optional<_TValue> getAttachment(PasteworkAttachmentType<_TValue> attachment)
    {
        IAttachmentHolder thiz = getAttachmentHolder();
        var neoAttachmentType = getAttachmentRegistry().getNeoAttachmentType(attachment);

        if(neoAttachmentType != null && thiz.hasData(neoAttachmentType))
        {
            return Optional.of(thiz.getData(neoAttachmentType));
        }

        return Optional.empty();
    }

    @Override
    default boolean hasAttachment(PasteworkAttachmentType<?> attachment)
    {
        IAttachmentHolder thiz = getAttachmentHolder();
        var neoAttachmentType = getAttachmentRegistry().getNeoAttachmentType(attachment);

        if (neoAttachmentType != null)
        {
            return thiz.hasData(neoAttachmentType);
        }

        return false;
    }

    @Override
    default boolean detachAttachment(PasteworkAttachmentType<?> attachment)
        throws RegistryException, WrongThreadException
    {
        IAttachmentHolder thiz = getAttachmentHolder();
        var neoAttachmentType = getAttachmentRegistry().getNeoAttachmentType(attachment);

        if (neoAttachmentType != null && thiz.hasData(neoAttachmentType))
        {
            thiz.removeData(neoAttachmentType);
            return true;
        }

        return false;
    }
}
