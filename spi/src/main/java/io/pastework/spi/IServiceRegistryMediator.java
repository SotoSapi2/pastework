package io.pastework.spi;

/**
 * Mediator interface for service implementation registration.
 *
 * @since 1.0.0
 */
public interface IServiceRegistryMediator extends IServiceHolder
{
    /**
     * Registers a service implementation based on its interface mapping.
     *
     * @param mapType The interface class of the service being registered.
     * @param service The service implementation instance.
     * @param <_TService> The type of the service.
     * @throws IllegalStateException if services registration is already finalized.
     */
    <_TService extends IPasteworkService>
    void registerService(Class<_TService> mapType, _TService service);
}
