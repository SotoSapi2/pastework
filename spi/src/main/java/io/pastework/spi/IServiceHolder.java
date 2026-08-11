package io.pastework.spi;

/**
 * Interface that define service implementation container.
 *
 * @since 1.0.0
 */
public interface IServiceHolder
{
    /**
     * Gets a service implementation based on its interface mapping.
     *
     * @param klass The interface class of the requested service.
     * @param <_TService> The type of the service.
     * @return The specified implementation instance.
     * @throws NullPointerException if the expected service doesn't have implementation.
     */
    <_TService extends IPasteworkService>
    _TService getService(Class<_TService> klass);

    /**
     * Checks if a specific service is registered.
     *
     * @param klass The class of the service to check.
     * @return {@code true} if the service is registered, {@code false} otherwise.
     */
    boolean isServiceRegistered(Class<? extends IPasteworkService> klass);
}
