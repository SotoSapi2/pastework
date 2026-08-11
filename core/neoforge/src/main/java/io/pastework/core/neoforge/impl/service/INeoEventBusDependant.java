package io.pastework.core.neoforge.impl.service;

import net.neoforged.bus.api.IEventBus;

public interface INeoEventBusDependant
{
    void handleEventBus(IEventBus eventBus);
}
