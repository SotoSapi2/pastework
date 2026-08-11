package io.pastework.core.api.common.service.registry;

/**
 * A default implementation of {@link IEntryHolder}.
 *
 * @param <TEntry> The type of the entry.
 */
public class DefaultEntryHolder<TEntry> implements IEntryHolder<TEntry>
{
    private final String name;
    private final TEntry entry;

    public DefaultEntryHolder(String name, TEntry entry)
    {
        this.name = name;
        this.entry = entry;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public TEntry getEntry()
    {
        return entry;
    }
}
