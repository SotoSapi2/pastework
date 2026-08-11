package io.pastework.core.api.common.service.network;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.server.network.ConfigurationTask;

import java.util.Collection;

/**
 * A configurator interface used during the network configuration phase to register tasks and handle connection setup.
 */
public interface INetworkConfigurator
{
    /**
     * Gets the network connection associated with this configurator.
     *
     * @return The network {@link Connection}.
     */
    Connection getConnection();

    /**
     * Gets the server configuration packet listener.
     *
     * @return {@link ServerConfigurationPacketListener} that currently listening to this channel.
     */
    ServerConfigurationPacketListener getListener();

    /**
     * Checks whether the given packet type can be accepted by the configurator.
     *
     * @param type The type of the custom packet payload.
     * @return {@code true} if the type is accepted, {@code false} otherwise.
     * @throws UnsupportedOperationException If checking the acceptance is unsupported by the implementation.
     */
    boolean canAccept(CustomPacketPayload.Type<?> type) throws UnsupportedOperationException;

    /**
     * Registers a new configuration task to be processed during the configuration phase.
     *
     * @param task The configuration task to register.
     */
    void registerTask(ConfigurationTask task);

    /**
     * Retrieves all registered configuration tasks.
     *
     * @return A {@link Collection} of the registered {@link ConfigurationTask}.
     */
    Collection<ConfigurationTask> getRegisteredTasks();
}
