package io.pastework.core.api.client.service.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import io.pastework.core.api.Pastework;
import net.minecraft.client.KeyMapping;

import java.util.Collection;

public interface IKeyMappingRegistrar
{
    static IKeyMappingRegistrar create()
    {
        return Pastework.INSTANCE.getService(IKeyMappingRegistry.class)
            .createSet();
    }

    KeyMapping register(KeyMapping entry);

    default KeyMapping register(String name, int keyCode, KeyMapping.Category category)
    {
        return register(new KeyMapping(name, keyCode, category));
    }

    default KeyMapping register(String name, InputConstants.Type type, int keyCode, KeyMapping.Category category)
    {
        return register(new KeyMapping(name, type, keyCode, category));
    }

    Collection<KeyMapping> getKeyMappings();
}
