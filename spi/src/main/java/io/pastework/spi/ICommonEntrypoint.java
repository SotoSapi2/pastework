package io.pastework.spi;

/**
 * Entrypoint SPI called by the framework
 * on both server and client physical side after Pastework finishes the initialization phase.
 * <p>
 * Use {@link IClientEntrypoint} for entrypoints that only runs on client.
 *
 * @since 1.0.0
 * @see IClientEntrypoint
 */
public interface ICommonEntrypoint
{
    /**
     * The method that will be called by the framework as entrypoint.
     */
    void commonMain();
}
