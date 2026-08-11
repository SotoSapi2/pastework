package io.pastework.core.base.common.impl.internal;

import io.pastework.core.api.Pastework;
import io.pastework.core.api.common.platform.SideType;
import io.pastework.core.api.spi.IRuntimeInformation;
import io.pastework.spi.*;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@ApiStatus.Internal
public final class PasteworkImpl implements Pastework, IServiceRegistryMediator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PasteworkImpl.class);

    private volatile boolean isDependantInvoked;
    private volatile boolean isServiceRegistrationFinalized;
    private final IRuntimeInformation runtimeInformation;
    private final Map<Class<? extends IPasteworkService>, IPasteworkService> serviceMap;

    public PasteworkImpl()
    {
        this.serviceMap = new HashMap<>();
        this.runtimeInformation = ServiceLoader.load(IRuntimeInformation.class)
            .findFirst()
            .orElseThrow(() -> new NullPointerException("Couldn't resolve IRuntimeInformation implementation."));
    }

    @Override
    @SuppressWarnings("unchecked")
    public
    <_TService extends IPasteworkService>
    _TService getService(Class<_TService> klass)
    {
        if(!serviceMap.containsKey(klass))
        {
            throw new NullPointerException(String.format(
                "'%s' is unavailable.",
                klass.getName()
            ));
        }

        return (_TService) serviceMap.get(klass);
    }

    @Override
    public boolean isServiceRegistered(Class<? extends IPasteworkService> klass)
    {
        return serviceMap.containsKey(klass);
    }

    @Override
    public Collection<IPasteworkService> getServices()
    {
        throwIfServiceUnprepared();
        return serviceMap.values();
    }

    public void prepareServices()
    {
        throwIfServiceFinalized();

        final var commonRegistratorList = ServiceLoader.load(ICommonServiceRegistrator.class)
            .stream()
            .map(ServiceLoader.Provider::get)
            .toList();

        LOGGER.info("Found {} common service registrators.", commonRegistratorList.size());

        for(var registrator : commonRegistratorList)
        {
            LOGGER.info("Invoking {} common service registrator.", registrator.getClass().getName());
            registrator.prepareCommonServices(this);
        }

        if(runtimeInformation.getEnvironmentType() == SideType.CLIENT)
        {
            final var clientRegistratorList = ServiceLoader.load(IClientServiceRegistrator.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .toList();

            LOGGER.info("Found {} client service registrators.", clientRegistratorList.size());

            for(var registrator : clientRegistratorList)
            {
                LOGGER.info("Invoking {} client service registrator.", registrator.getClass().getName());
                registrator.prepareClientServices(this);
            }
        }

        isServiceRegistrationFinalized = true;
    }

    @Override
    public boolean isInitialized()
    {
        return isServiceRegistrationFinalized && isDependantInvoked;
    }

    @Override
    public
    <_TService extends IPasteworkService>
    void registerService(Class<_TService> mapType, _TService service)
    {
        throwIfServiceFinalized();

        serviceMap.put(mapType, service);

        LOGGER.info("Service {} registered as {}.",
            mapType.getName(),
            service.getClass().getName()
        );
    }

    @Override
    public IRuntimeInformation getRuntimeInformation()
    {
        return runtimeInformation;
    }

    public void invokeEveryDependantEntrypoint()
    {
        throwIfServiceUnprepared();

        if(isDependantInvoked)
        {
            throw new IllegalStateException("Dependants are already been invoked.");
        }

        isDependantInvoked = true;

        final var commonEntrypointList = ServiceLoader.load(ICommonEntrypoint.class)
            .stream()
            .map(ServiceLoader.Provider::get)
            .toList();

        LOGGER.info("Found {} total common entrypoint.", commonEntrypointList.size());

        for (var entrypoint : commonEntrypointList)
        {
            LOGGER.info("Invoking {} common entrypoint.", entrypoint.getClass().getName());
            entrypoint.commonMain();
        }

        if(runtimeInformation.getEnvironmentType() == SideType.CLIENT)
        {
            var clientEntrypointList = ServiceLoader.load(IClientEntrypoint.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .toList();

            LOGGER.info("Found {} total client entrypoint.", clientEntrypointList.size());

            for (var entrypoint : clientEntrypointList)
            {
                LOGGER.info("Invoking {} client entrypoint.", entrypoint.getClass().getName());
                entrypoint.clientMain();
            }
        }
    }

    private void throwIfServiceUnprepared()
    {
        if(!isServiceRegistrationFinalized)
        {
            throw new IllegalStateException("Accessing too early! Service registration haven't been done yet.");
        }
    }

    private void throwIfServiceFinalized()
    {
        if(isServiceRegistrationFinalized)
        {
            throw new IllegalStateException("Service registration is already been finalized.");
        }
    }
}
