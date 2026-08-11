package io.pastework.core.fabric.common.impl.service;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface IFabricRegistrable
{
    boolean isRegistrationFinalized();

    void processRegistration();
}
