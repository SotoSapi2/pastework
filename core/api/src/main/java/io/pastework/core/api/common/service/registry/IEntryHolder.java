package io.pastework.core.api.common.service.registry;

import net.minecraft.resources.Identifier;

/**
 * Represents holder for an entry in a registry.
 * <p>
 * For usage constraint and example, see the package documentation:
 * {@link io.pastework.core.api.common.service.registry}
 *
 * @param <TEntry> The type of the entry.
 */
public interface IEntryHolder<TEntry>
{
    /**
     * Gets the name of the entry.
     *
     * @return The name of the entry.
     */
    String getName();

    /**
     * Gets the entry.
     *
     * @return The entry.
     */
    TEntry getEntry();

    /**
     * Creates an Identifier for this entry using the specified namespace.
     *
     * @param namespace The namespace to use.
     * @return The Identifier for this entry.
     */
    default Identifier createIdentifier(String namespace)
    {
        return Identifier.fromNamespaceAndPath(namespace, getName());
    }
}
