package io.pastework.core.api.client.service.network;

import io.pastework.core.api.Pastework;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.ApiStatus;

/**
 * Provides an interface for sending custom packets from the client to the server during play phase.
 */
@ApiStatus.NonExtendable
public interface IPlayServerRemote
{
    /**
     * Retrieves the instance of the remote server handle from the client networking service.
     *
     * @return The play server remote handle.
     */
    static IPlayServerRemote getRemote()
    {
        return Pastework.INSTANCE
            .getService(IClientNetworking.class)
            .getPlayRemote();
    }

    /**
     * Sends a custom packet payload from the client to the server.
     *
     * @param payload The custom packet payload to send.
     */
    void send(CustomPacketPayload payload);
}
