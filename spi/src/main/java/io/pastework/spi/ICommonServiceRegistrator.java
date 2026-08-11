package io.pastework.spi;

/**
 * SPI handling service registration during the framework initialization phase.
 * <p>
 * This interface resolved on both client and server physical environment side.
 * For client only registration use {@link IClientServiceRegistrator}
 *
 * @since 1.0.0
 * @see IClientServiceRegistrator
 */
public interface ICommonServiceRegistrator
{
    /**
     * The method would be called during the framework initialization phase.
     *
     * @param registryMediator object passed by the framework to register service
     */
    void prepareCommonServices(IServiceRegistryMediator registryMediator);
}
