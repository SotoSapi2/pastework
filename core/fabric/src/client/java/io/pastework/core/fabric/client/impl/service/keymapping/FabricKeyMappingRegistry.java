package io.pastework.core.fabric.client.impl.service.keymapping;

import io.pastework.core.api.client.service.keymapping.IKeyMappingRegistrar;
import io.pastework.core.base.client.impl.service.keymapping.AbstractKeyMappingRegistry;
import io.pastework.core.fabric.common.impl.service.IFabricRegistrable;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class FabricKeyMappingRegistry extends AbstractKeyMappingRegistry implements IFabricRegistrable
{
    @Override
    public boolean isRegistrationFinalized()
    {
        return super.isRegistrationFinalized();
    }

    @Override
    public void processRegistration()
    {
        if (isRegistrationFinalized())
        {
            throw new IllegalStateException();
        }

        for (IKeyMappingRegistrar registrar : getRegistrarSet())
        {
            registrar.getKeyMappings()
                .forEach(KeyBindingHelper::registerKeyBinding);
        }

        finalizeRegistration();
    }
}
