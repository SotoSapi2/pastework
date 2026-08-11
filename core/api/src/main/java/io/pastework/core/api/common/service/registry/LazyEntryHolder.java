package io.pastework.core.api.common.service.registry;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Entry holder that defers the creation of the entry until it's first requested.
 *
 * @param <TEntry> The type of the entry being held.
 */
public class LazyEntryHolder<TEntry> implements IEntryHolder<TEntry>
{
    private final String name;
    private final Supplier<TEntry> supplier;
    private @Nullable TEntry entry;

    public LazyEntryHolder(String name, Supplier<TEntry> supplier)
    {
        this.name = name;
        this.supplier = supplier;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public TEntry getEntry()
    {
        if(entry == null)
        {
            entry = supplier.get();
        }

        return entry;
    }
}
