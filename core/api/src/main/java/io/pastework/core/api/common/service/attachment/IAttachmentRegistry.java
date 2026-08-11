package io.pastework.core.api.common.service.attachment;

import io.pastework.core.api.Pastework;
import io.pastework.spi.IPasteworkService;

/**
 * Service registry for managing attachment types and registrars.
 */
public interface IAttachmentRegistry extends IPasteworkService
{
    /**
     * Gets the singleton instance of the {@link IAttachmentRegistry} service.
     *
     * @return The attachment registry service.
     */
    static IAttachmentRegistry getService()
    {
        return Pastework.INSTANCE.getService(IAttachmentRegistry.class);
    }

    /**
     * Checks if the attachment registration phase is finalized.
     *
     * @return {@code true} if registration is finalized, {@code false} otherwise.
     */
    boolean isRegistrationFinalized();

    /**
     * Creates a new attachment registrar for a specific namespace.
     *
     * @param namespace The namespace for the registrar (e.g., a mod ID).
     * @return The newly created attachment registrar.
     */
    IAttachmentRegistrar createRegistrySet(String namespace);

    /**
     * Enqueues an attachment registrar to be processed by the registry.
     *
     * @param holder The attachment registrar to enqueue.
     */
    void enqueueRegistrar(IAttachmentRegistrar holder);
}
