package io.pastework.core.api.common.service.attachment;

import io.pastework.core.api.Pastework;
import io.pastework.core.api.common.service.registry.IEntryHolder;

import java.util.Collection;

/**
 * Registrar scoped to a specific namespace for registering custom attachment types.
 */
public interface IAttachmentRegistrar
{
    /**
     * Creates a new attachment registrar with the specified namespace.
     *
     * @param namespace The namespace for the newly created registrar.
     * @return A new attachment registrar instance.
     */
    static IAttachmentRegistrar create(String namespace)
    {
        return Pastework.INSTANCE.getService(IAttachmentRegistry.class)
            .createRegistrySet(namespace);
    }

    /**
     * Gets the namespace associated with this registrar.
     *
     * @return The namespace.
     */
    String getNamespace();

    /**
     * Registers a new attachment type under this registrar.
     *
     * @param name        The name of the attachment type.
     * @param entryHolder The attachment type definition to register.
     * @param <_TAttachment> The type of data stored in this attachment.
     * @return The registered attachment type instance.
     */
    <_TAttachment>
    PasteworkAttachmentType<_TAttachment> register(
        String name,
        PasteworkAttachmentType<_TAttachment>  entryHolder
    );

    /**
     * Retrieves all attachment type holders registered in this registrar.
     *
     * @return A collection of all registered entry holders.
     */
    Collection<IEntryHolder<PasteworkAttachmentType<?>>> getHolders();
}
