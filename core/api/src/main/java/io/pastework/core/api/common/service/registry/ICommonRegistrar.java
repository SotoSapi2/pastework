package io.pastework.core.api.common.service.registry;

import io.pastework.core.api.Pastework;
import io.pastework.core.api.exception.RegistryException;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Interface representing a holder for a specific registry type.
 * <p>
 * This interface is responsible for enqueuing entries that will be passed to {@link ICommonRegistry}
 * for the game to process. It also acts as a container for the entry
 * without referencing directly to the native registry.
 * <p>
 * For usage constraint and example, see the package documentation:
 * {@link io.pastework.core.api.common.service.registry}
 *
 * @param <TBaseEntry> The type of elements in the registry.
 * @see ICommonRegistry
 * @see RegistryException
 * @since 1.0.0
 */
public interface ICommonRegistrar<TBaseEntry>
{
    /**
     * Factory method to create a registry holder for the specified registry key.
     *
     * @param registryKey The resource key of the target registry.
     * @param <_TBaseEntry> The type of elements in the registry.
     * @return An instance of IRegistryHolder for the specified registry.
     */
    static
    <_TBaseEntry>
    ICommonRegistrar<_TBaseEntry> create(
        String namespace,
        ResourceKey<? extends Registry<_TBaseEntry>> registryKey
    )
    {
        return Pastework.INSTANCE.getService(ICommonRegistry.class)
            .createRegistrar(namespace, registryKey);
    }

    /**
     * Checks if the registration process has been finalized.
     *
     * @return {@code true} if registration is finalized, {@code false} otherwise.
     */
    boolean isRegistrationFinalized();

    /**
     * Gets the namespace associated with this registrar.
     *
     * @return The namespace as a string.
     */
    String getNamespace();

    /**
     * Gets the resource key of the target registry.
     *
     * @return The resource key of the registry.
     */
    ResourceKey<? extends Registry<TBaseEntry>> getRegistryKey();

    /**
     * Registers a deferred entry holder with this registrar.
     *
     * @param entryHolder The deferred entry holder to register.
     * @param <_TEntry>         The type of the entry being registered.
     * @return The registered deferred entry holder.
     */
    <_TEntry extends TBaseEntry>
    IEntryHolder<_TEntry> register(LazyEntryHolder<_TEntry> entryHolder);

    /**
     * Registers a new entry with this registrar using a supplier.
     *
     * @param name  The name of the entry.
     * @param entry The supplier that provides the entry instance.
     * @param <_TEntry> The type of the entry being registered.
     * @return The registered deferred entry holder.
     */
    default <_TEntry extends TBaseEntry>
    IEntryHolder<_TEntry> register(String name, Supplier<_TEntry> entry)
    {
        LazyEntryHolder<_TEntry> holder = new LazyEntryHolder<>(name, entry);
        return register(holder);
    }

    /**
     * Retrieves all registered deferred entry holders.
     *
     * @return immutable collection of deferred entry holders.
     */
    Collection<LazyEntryHolder<? extends TBaseEntry>> getEntries();
}
