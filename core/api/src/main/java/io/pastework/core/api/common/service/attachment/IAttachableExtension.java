package io.pastework.core.api.common.service.attachment;

import io.pastework.core.api.exception.RegistryException;
import org.jetbrains.annotations.ApiStatus;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Interface for managing attachment for target objects.
 * <p>
 * This interface is extension for classes listed below:
 * <br>
 * - {@link net.minecraft.world.entity.Entity}
 * <br>
 * - {@link net.minecraft.world.level.chunk.ChunkAccess}
 * <br>
 * - {@link net.minecraft.world.level.block.entity.BlockEntity}
 * <br>
 * - {@link net.minecraft.world.level.Level}
 */
@ApiStatus.NonExtendable
public interface IAttachableExtension
{
    @FunctionalInterface
    interface ModifierCallback<_TValue>
    {
        _TValue invoke(_TValue previousValue);
    }

    /**
     * @throws RegistryException when the passed attachment is not registered.
     * @throws WrongThreadException when this method is not called from server thread.
     */
    <_TValue>
    _TValue emplaceAttachment(PasteworkAttachmentType<_TValue> attachment)
        throws RegistryException, WrongThreadException;

    /**
     * @throws RegistryException when the passed attachment is not registered.
     * @throws WrongThreadException when this method is not called from server thread.
     */
    <_TValue>
    void setAttachment(PasteworkAttachmentType<_TValue> attachment, _TValue value)
        throws RegistryException, WrongThreadException;

    /**
     * @throws RegistryException when the passed attachment is not registered.
     * @throws WrongThreadException when this method is not called from server thread.
     */
    <_TValue>
    _TValue editAttachment(
        PasteworkAttachmentType<_TValue> attachment,
        ModifierCallback<_TValue> modifierCallback
    ) throws NoSuchElementException, RegistryException;

    /**
     * @throws RegistryException when the passed attachment is not registered.
     * @throws WrongThreadException when this method is not called from server thread.
     */
    default
    <_TValue>
    _TValue editOrEmplaceAttachment(
        PasteworkAttachmentType<_TValue> attachment,
        ModifierCallback<_TValue> modifierCallback
    ) throws RegistryException, WrongThreadException
    {
        if (!hasAttachment(attachment))
        {
            emplaceAttachment(attachment);
        }

        return editAttachment(attachment, modifierCallback);
    }

    <_TValue>
    Optional<_TValue> getAttachment(PasteworkAttachmentType<_TValue> attachment);

    default
    <_TValue>
    _TValue getOrThrow(PasteworkAttachmentType<_TValue> attachment) throws NoSuchElementException
    {
        return getAttachment(attachment).orElseThrow();
    }

    /**
     * @throws RegistryException when the passed attachment is not registered.
     * @throws WrongThreadException when this method is not called from server thread.
     */
    default
    <_TValue>
    _TValue
    getOrEmplace(PasteworkAttachmentType<_TValue> attachment) throws RegistryException, WrongThreadException
    {
        //Pastework.assertServerThread("IAttachmentService::getOrEmplace must be called from server thread.");

        return getAttachment(attachment)
            .orElseGet(() -> emplaceAttachment(attachment));
    }

    /**
     * @throws RegistryException when the passed attachment is not registered.
     * @throws WrongThreadException when this method is not called from server thread.
     */
    default
    <_TValue>
    _TValue getOrSetAttachment(PasteworkAttachmentType<_TValue> attachment, _TValue value)
        throws RegistryException, WrongThreadException
    {
        //Pastework.assertServerThread("IAttachmentService::getOrSet must be called from server thread.");

        return getAttachment(attachment)
            .orElseGet(() ->
            {
                setAttachment(attachment, value);
                return value;
            });
    }

    boolean hasAttachment(PasteworkAttachmentType<?> attachment);

    /**
     * @throws RegistryException when the passed attachment is not registered.
     * @throws WrongThreadException when this method is not called from server thread.
     */
    boolean detachAttachment(PasteworkAttachmentType<?> attachment)
        throws RegistryException, WrongThreadException;
}
