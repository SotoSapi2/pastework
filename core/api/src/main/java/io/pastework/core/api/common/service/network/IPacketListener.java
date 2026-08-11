package io.pastework.core.api.common.service.network;

/**
 * Listener interface for handling incoming network packets.
 *
 * @param <TPayload> The payload type this listener can handle.
 */
public interface IPacketListener<TPayload>
{
    /**
     * Handles an incoming packet payload.
     *
     * @param payload The custom packet payload received.
     * @param ctx     The packet context containing connection and sender information.
     */
    void handlePacket(TPayload payload, IPacketContext ctx);
}
