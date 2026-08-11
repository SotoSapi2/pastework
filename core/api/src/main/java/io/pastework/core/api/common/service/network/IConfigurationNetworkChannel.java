package io.pastework.core.api.common.service.network;

import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Consumer;

/**
 * A network channel responsible configuring client setup while they're
 * connecting to a server during configuration phase.
 * <p>
 * {@link INetworkConfigurator} registered by this channel is responsible to register task to the client and managing
 * their connection.
 *
 * @see INetworkConfigurator
 */
public interface IConfigurationNetworkChannel extends INetworkChannel<FriendlyByteBuf>
{
    /**
     * Enqueues a configuration task to be executed during the configuration phase.
     *
     * @param consumer A consumer acting on the {@link INetworkConfigurator} to register configuration tasks.
     */
    void enqueueConfiguration(Consumer<INetworkConfigurator> consumer);
}
