package io.pastework.core.api.client.service.keymapping;

import io.pastework.core.api.Pastework;
import io.pastework.spi.IPasteworkService;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface IKeyMappingRegistry extends IPasteworkService
{
    static IKeyMappingRegistry getService()
    {
        return Pastework.INSTANCE.getService(IKeyMappingRegistry.class);
    }

    boolean isRegistrationFinalized();

    IKeyMappingRegistrar createSet();

    void enqueueRegistrar(IKeyMappingRegistrar holder);
}
