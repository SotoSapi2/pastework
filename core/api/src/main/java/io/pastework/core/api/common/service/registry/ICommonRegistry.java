package io.pastework.core.api.common.service.registry;

import io.pastework.core.api.Pastework;
import io.pastework.core.api.exception.RegistryException;
import io.pastework.spi.IPasteworkService;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

/**
 * Service for abstracting registry operations across different loaders.
 * Provides methods to access vanilla/modded registries, create new ones and
 * enqueue entries from {@link ICommonRegistrar} to be processed by the game.
 * <p>
 * For usage constraint and example, see the package documentation:
 * {@link io.pastework.core.api.common.service.registry}
 *
 * @see io.pastework.spi.ICommonEntrypoint
 * @see io.pastework.spi.IClientEntrypoint
 * @since 1.0.0
 */
public interface ICommonRegistry extends IPasteworkService
{
    /**
     * Retrieves the singleton instance of the registry service.
     *
     * @return The {@link ICommonRegistry} instance.
     */
    static ICommonRegistry getService()
    {
        return Pastework.INSTANCE.getService(ICommonRegistry.class);
    }

    /**
     * Checks if the registration process has been finalized.
     *
     * @return {@code true} if registration is finalized, {@code false} otherwise.
     */
    boolean isRegistrationFinalized();

    /**
     * Creates a registry holder for the specified registry key.
     *
     * @param <_TBaseEntry> The type of elements held by the registry.
     * @param registryKey The resource key identifying the registry.
     * @return An {@link ICommonRegistrar} for the specified registry.
     */
    <_TBaseEntry>
    ICommonRegistrar<_TBaseEntry> createRegistrar(
        String namespace,
        ResourceKey<? extends Registry<_TBaseEntry>> registryKey
    );

    /**
     * Enqueues the given registry holder for registration.
     * <p>
     * This method schedules the registration of all entries contained in the
     * provided {@link ICommonRegistrar}. The actual registration process may occur
     * at a later stage, depending on the underlying implementation.
     *
     * @param <_TBaseEntry> The type of elements held by the registry.
     * @param holder The registry holder containing entries to be registered.
     * @throws RegistryException if the registry queue is already complete or finalized.
     */
    <_TBaseEntry>
    void enqueueRegistrar(ICommonRegistrar<_TBaseEntry> holder)
        throws RegistryException;

    /**
     * Enqueues the creation of a new registry with the given key.
     *
     * @param <_TBaseEntry> The type of elements held by the registry.
     * @param key The resource key identifying the new registry.
     * @param synced Whether the registry numerical IDs synced to clients.
     * @throws RegistryException if the registry queue is already complete or finalized.
     */
    <_TBaseEntry>
    void enqueueNewRegistry(ResourceKey<? extends Registry<_TBaseEntry>> key, boolean synced)
        throws RegistryException;

    /**
     * Enqueues a new registry with a default value.
     * <p>
     * If a registry lookup fails to find a value, the value associated with the
     * default element key will be returned instead.
     *
     * @param <_TBaseEntry> The type of elements held by the registry.
     * @param key The resource key identifying the new registry.
     * @param defaultEntryKey The resource location of the default entry.
     * @param synced Whether the registry numerical IDs get synced to clients.
     * @throws RegistryException if the registry queue is already complete or finalized.
     */
    <_TBaseEntry>
    void enqueueNewRegistryDefaulted(
        ResourceKey<? extends Registry<_TBaseEntry>> key,
        Identifier defaultEntryKey,
        boolean synced
    ) throws RegistryException;

    /**
     * Enqueues the creation of a new registry with the given key.
     * <p>
     * This method is overload of {@link ICommonRegistry#enqueueNewRegistry(ResourceKey, boolean)} with {@code synced}
     * parameter passed with {@code true} as default value.
     *
     * @param <_TBaseEntry> The type of elements held by the registry.
     * @param key The resource key identifying the new registry.
     * @throws RegistryException if the registry queue is already complete or finalized.
     */
    default
    <_TBaseEntry>
    void enqueueNewRegistry(ResourceKey<? extends Registry<_TBaseEntry>> key)
        throws RegistryException
    {
        enqueueNewRegistry(key, true);
    }

    /**
     * Enqueues a new registry with a default value.
     * <p>
     * If a registry lookup fails to find a value, the value associated with the
     * default element key will be returned instead.
     * <p>
     * This method is overload of {@link ICommonRegistry#enqueueNewRegistryDefaulted(ResourceKey, Identifier, boolean)}
     * with {@code synced} parameter passed with {@code true} as default value.
     *
     * @param <_TBaseEntry> The type of elements held by the registry.
     * @param key The resource key identifying the new registry.
     * @param defaultEntryKey The resource location of the default entry.
     * @throws RegistryException if the registry queue is already complete or finalized.
     */
    default
    <_TBaseEntry>
    void enqueueNewRegistryDefaulted(
        ResourceKey<? extends Registry<_TBaseEntry>> key,
        Identifier defaultEntryKey
    ) throws RegistryException
    {
        enqueueNewRegistryDefaulted(key, defaultEntryKey, true);
    }

    /**
     * Enqueues an existing registry instance for registration.
     *
     * @param <_TBaseEntry> The type of elements held by the registry.
     * @param key The resource key identifying the registry.
     * @param registry The registry instance to enqueue.
     * @throws RegistryException if the registry queue is already complete or finalized.
     */
    <_TBaseEntry>
    void enqueueNewRegistryInstance(
        ResourceKey<? extends Registry<_TBaseEntry>> key,
        Registry<_TBaseEntry> registry
    ) throws RegistryException;

    /**
     * Retrieves the native registry object associated with the given resource key.
     * <p>
     * In Forge and NeoForge, registry initialization is deferred.
     * Calling this method before the registry is initialized will result in a
     * {@link RegistryException} being thrown.
     * <p>
     * If registry with the specified key cannot be found this method will also throw
     * {@link RegistryException}.
     *
     * @param <_TBaseEntry> The type of elements held by the registry.
     * @param key The resource key identifying the registry.
     * @return The requested {@link Holder.Reference} of the registry.
     *
     * @throws RegistryException Explained on this method description (see above).
     */
    <_TBaseEntry>
    Holder.Reference<? extends Registry<_TBaseEntry>> requestNativeRegistry(
        ResourceKey<? extends Registry<_TBaseEntry>> key
    ) throws RegistryException;
}