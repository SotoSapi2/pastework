package io.pastework.core.api.common.service.network;

import io.pastework.core.api.Pastework;
import io.pastework.spi.IPasteworkService;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Service for managing server-side networking.
 * <p>
 * Provides methods to register global packet receivers and access the remote client handles
 * for sending packets from server to client.
 */
public interface IServerNetworking extends IPasteworkService
{
    /**
     * Gets the singleton instance of the {@link IServerNetworking} service.
     *
     * @return The server networking service.
     */
    static IServerNetworking getService()
    {
        return Pastework.INSTANCE.getService(IServerNetworking.class);
    }

    /**
     * Gets the remote client interface for sending custom packet payloads.
     *
     * @return The remote client handler.
     */
    IPlayClientRemote getPlayRemote();

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
