package io.pastework.spi;

/**
 * Entrypoint SPI called by the framework, only if the current physical side is client
 * after Pastework finishes the initialization phase.
 * <p>
 * Resolves after the {@link ICommonEntrypoint} execution finishes.
 * <p>
 * Use {@link ICommonEntrypoint} for entrypoints that run on both the client and server.
 *
 * @since 1.0.0
 * @see ICommonEntrypoint
 */

public interface IClientEntrypoint
{
    /**
     * The method that will be called by the framework as entrypoint.
     */
    void clientMain();
}
