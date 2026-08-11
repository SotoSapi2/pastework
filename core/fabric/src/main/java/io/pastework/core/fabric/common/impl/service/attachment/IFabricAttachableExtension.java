package io.pastework.core.fabric.common.impl.service.attachment;

import io.pastework.core.api.Pastework;
import io.pastework.core.api.common.service.attachment.PasteworkAttachmentType;
import io.pastework.core.api.common.service.attachment.IAttachableExtension;
import io.pastework.core.api.common.service.attachment.IAttachmentRegistry;
import io.pastework.core.api.exception.RegistryException;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;

import java.util.NoSuchElementException;
import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public interface IFabricAttachableExtension extends IAttachableExtension
{
    AttachmentTarget getAttachmentTarget();

    default FabricAttachmentRegistry getAttachmentRegistry()
    {
        IAttachmentRegistry attachmentRegistry = Pastework.INSTANCE.getService(IAttachmentRegistry.class);

        if(attachmentRegistry instanceof FabricAttachmentRegistry fabricAttachmentRegistry)
        {
            return fabricAttachmentRegistry;
        }

        throw new IllegalStateException("IAttachmentRegistry service must be an instance of FabricAttachmentRegistry.");
    }

    @Override
    default
    <_TValue>
    _TValue emplaceAttachment(PasteworkAttachmentType<_TValue> attachment)
        throws RegistryException, WrongThreadException
    {
        AttachmentTarget thiz = getAttachmentTarget();
        var neoAttachmentType = getAttachmentRegistry().getFabricAttachmentType(attachment);
        var value = attachment.getInitializer().get();

        thiz.setAttached(neoAttachmentType, value);
        return value;
    }

    @Override
    default
    <_TValue>
    void setAttachment(PasteworkAttachmentType<_TValue> attachment, _TValue value)
        throws RegistryException, WrongThreadException
    {
        AttachmentTarget thiz = getAttachmentTarget();
        var neoAttachmentType = getAttachmentRegistry().getFabricAttachmentType(attachment);

        thiz.setAttached(neoAttachmentType, value);
    }

    @Override
    default <_TValue> _TValue editAttachment(
        PasteworkAttachmentType<_TValue> attachment,
        IAttachableExtension.ModifierCallback<_TValue> modifier
    ) throws NoSuchElementException, RegistryException
    {
        AttachmentTarget thiz = getAttachmentTarget();
        var neoAttachmentType = getAttachmentRegistry().getFabricAttachmentType(attachment);

        if(!thiz.hasAttached(neoAttachmentType))
        {
            throw new NoSuchElementException("Trying to attach unregistered attachment.");
        }

        var origValue = thiz.getAttached(neoAttachmentType);
        var newValue = modifier.invoke(origValue);

        thiz.setAttached(neoAttachmentType, newValue);
        return newValue;
    }

    @Override
    default
    <_TValue>
    Optional<_TValue> getAttachment(PasteworkAttachmentType<_TValue> attachment)
    {
        AttachmentTarget thiz = getAttachmentTarget();
        var neoAttachmentType = getAttachmentRegistry().getFabricAttachmentType(attachment);

        if(neoAttachmentType != null && thiz.hasAttached(neoAttachmentType))
        {
            return Optional.ofNullable(thiz.getAttached(neoAttachmentType));
        }

        return Optional.empty();
    }

    @Override
    default boolean hasAttachment(PasteworkAttachmentType<?> attachment)
    {
        AttachmentTarget thiz = getAttachmentTarget();
        var neoAttachmentType = getAttachmentRegistry().getFabricAttachmentType(attachment);

        if (neoAttachmentType != null)
        {
            return thiz.hasAttached(neoAttachmentType);
        }

        return false;
    }

    @Override
    default boolean detachAttachment(PasteworkAttachmentType<?> attachment)
        throws RegistryException, WrongThreadException
    {
        AttachmentTarget thiz = getAttachmentTarget();
        var neoAttachmentType = getAttachmentRegistry().getFabricAttachmentType(attachment);

        if (neoAttachmentType != null && thiz.hasAttached(neoAttachmentType))
        {
            thiz.removeAttached(neoAttachmentType);
            return true;
        }

        return false;
    }
}
