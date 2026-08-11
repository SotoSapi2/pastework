package io.pastework.core.base.client.impl.service.keymapping;

import io.pastework.core.api.client.service.keymapping.IKeyMappingRegistrar;
import net.minecraft.client.KeyMapping;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DefaultKeyMappingRegistrar implements IKeyMappingRegistrar
{
    private final Set<KeyMapping> keyMappingSet = new HashSet<>();

    @Override
    public KeyMapping register(KeyMapping entry)
    {
        keyMappingSet.add(entry);
        return entry;
    }

    @Override
    public Collection<KeyMapping> getKeyMappings()
    {
        return Collections.unmodifiableCollection(keyMappingSet);
    }
}
