package io.pastework.spi;

/**
 * SPI handling service registration during the framework initialization phase.
 * <p>
 * This interface resolved on the client physical environment side.
 * For both client and server side registration use {@link ICommonServiceRegistrator}
 *
 * @since 1.0.0
 * @see ICommonServiceRegistrator
 */
public interface IClientServiceRegistrator
{
    void prepareClientServices(IServiceRegistryMediator mediator);
}
