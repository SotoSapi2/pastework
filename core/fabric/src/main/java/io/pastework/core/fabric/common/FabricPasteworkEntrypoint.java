package io.pastework.core.fabric.common;


import io.pastework.core.base.common.hook.BootstrapHooks;
import io.pastework.core.base.common.impl.internal.PasteworkImpl;
import io.pastework.core.fabric.common.impl.service.IFabricRegistrable;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public final class FabricPasteworkEntrypoint implements ModInitializer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FabricPasteworkEntrypoint.class);
    private boolean isInitialized;

    @Override
    @ApiStatus.Internal
    public void onInitialize()
    {
        if (isInitialized)
        {
            throw new IllegalStateException();
        }

        final FabricLoader fabricLoader = FabricLoader.getInstance();
        final PasteworkImpl pastework = (PasteworkImpl) PasteworkImpl.INSTANCE;

        pastework.prepareServices();
        pastework.invokeEveryDependantEntrypoint();

        var registrableServiceStream = pastework.getServices()
            .stream()
            .filter(it -> it instanceof IFabricRegistrable)
            .map(it -> (IFabricRegistrable) it);

        for(IFabricRegistrable registrableService : registrableServiceStream.toList())
        {
            registrableService.processRegistration();
        }

        BootstrapHooks.fireCommonSetupEvent();

        switch (fabricLoader.getEnvironmentType())
        {
            case CLIENT -> BootstrapHooks.fireClientSetupEvent();
            case SERVER -> BootstrapHooks.fireServerSetupEvent();
            default -> throw new AssertionError();
        }

        BootstrapHooks.fireLoadingCompleteEvent();
        LOGGER.info("Pastework initialization complete.");
        isInitialized = true;
    }
}
