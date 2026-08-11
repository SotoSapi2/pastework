package io.pastework.core.api.client.service.network;

import io.pastework.core.api.Pastework;
import io.pastework.core.api.common.service.network.IPacketListener;
import io.pastework.spi.IPasteworkService;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Service for managing client-side networking.
 * <p>
 * Provides methods to register global packet receivers and access the remote server
 * for sending packets from client to server.
 */
public interface IClientNetworking extends IPasteworkService
{
    /**
     * Gets the singleton instance of the {@link IClientNetworking} service.
     *
     * @return The client networking service.
     */
    static IClientNetworking getService()
    {
        return Pastework.INSTANCE.getService(IClientNetworking.class);
    }

    /**
     * Gets the remote server interface for sending custom packet payloads.
     *
     * @return The remote server handler as {@link IPlayServerRemote}.
     */
    IPlayServerRemote getPlayRemote();

    /**
     * Registers a global packet receiver for the specified custom packet type.
     * 
     * @param type     The type of the custom packet payload.
     * @param listener The listener to handle incoming packets of the specified type.
     * @param <_TPacket> The packet payload type.
     * @return {@code true} if the receiver was successfully registered, {@code false} otherwise.
     */
    <_TPacket extends CustomPacketPayload>
    boolean registerGlobalReceiver(
        CustomPacketPayload.Type<_TPacket> type,
        IPacketListener<_TPacket> listener
    );

    /**
     * Unregisters a previously registered global packet receiver.
     *
     * @param type The type of the custom packet payload to unregister.
     */
    void unregisterGlobalReceiver(CustomPacketPayload.Type<?> type);
}
