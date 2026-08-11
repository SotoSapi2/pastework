package io.pastework.core.api.common.service.attachment;

import java.util.NoSuchElementException;

/**
 * Adapter interface for interacting with data attached to an object.
 */
public interface IAttachmentHolderAdapter
{
    /**
     * Retrieves the attached data of the specified attachment type.
     *
     * @param attachment The attachment type defining the data to retrieve.
     * @param <T>        The type of the attached data.
     * @return The attached data.
     * @throws NoSuchElementException If the attachment is not present and has no viable initial value.
     */
    <T> T get(PasteworkAttachmentType<T> attachment) throws NoSuchElementException;

    /**
     * Checks if the attachment holder has an instance of the specified attachment type.
     *
     * @param attachment The attachment type to check.
     * @return {@code true} if the attachment is present, {@code false} otherwise.
     */
    boolean has(PasteworkAttachmentType<?> attachment);

    /**
     * Evaluates if this adapter is wrapping the given target object instance.
     *
     * @param object The object to check against.
     * @return {@code true} if this adapter holds the given object, {@code false} otherwise.
     */
    boolean is(Object object);

    /**
     * Retrieves the original object instance that holds the attachments.
     *
     * @return The underlying holder instance.
     */
    Object getHolderInstance();
}
