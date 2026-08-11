package io.pastework.core.api.common.service.network;

import io.pastework.core.api.Pastework;
import io.pastework.spi.IPasteworkService;

/**
 * Service registry for accessing different network channels.
 */
public interface INetworkRegistry extends IPasteworkService
{
    /**
     * Gets the singleton instance of the {@link INetworkRegistry} service.
     *
     * @return The network registry service.
     */
    static INetworkRegistry getService()
    {
        return Pastework.INSTANCE.getService(INetworkRegistry.class);
    }

    /**
     * Retrieves the network channel used for server-side play phase communication.
     *
     * @return The play network channel for the server.
     */
    IPlayNetworkChannel forServerPlay();

    /**
     * Retrieves the network channel used for client-side play phase communication.
     *
     * @return The play network channel for the client.
     */
    IPlayNetworkChannel forClientPlay();

    /**
     * Retrieves the configuration network channel used by the client.
     *
     * @return The configuration network channel for the client.
     */
    IConfigurationNetworkChannel forClientConfig();
}
