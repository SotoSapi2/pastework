package io.pastework.core.api.common.service.network;

import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

/**
 * Provides context information and utility methods for handling a received network packet payload.
 */
@ApiStatus.NonExtendable
public interface IPacketContext
{
    /**
     * Gets the transmission direction of the packet.
     *
     * @return The packet flow direction (clientbound or serverbound).
     */
    PacketFlow getPacketFlow();

    /**
     * Gets the thread context type on which the packet listener is currently executing.
     *
     * @return The current network thread type. Always return {@link NetworkThreadType#MAIN} on fabric.
     */
    NetworkThreadType getNetworkThreadType();

    /**
     * Gets the underlying network connection associated with this packet transmission.
     *
     * @return The network connection.
     */
    Connection getConnection();

    /**
     * Gets the active packet listener processing the transmission.
     *
     * @return The packet listener.
     */
    PacketListener getListener();

    /**
     * Retrieves the player relevant to this payload. Players are only available in the {@link ConnectionProtocol#PLAY} phase.
     * <p>
     * For server-bound payloads, retrieves the sending {@link ServerPlayer}.
     * <p>
     * For client-bound payloads, retrieves the receiving {@code LocalPlayer}.
     *
     * @return The associated player.
     * @throws UnsupportedOperationException when called during the configuration phase.
     */
    Player getPlayer() throws UnsupportedOperationException;

    /**
     * Sends the given payload back to the sender.
     *
     * @param payload The payload to reply with.
     * @throws UnsupportedOperationException if the native implementation does not support the replying operation.
     */
    void reply(CustomPacketPayload payload) throws UnsupportedOperationException;

    /**
     * Gets the connection protocol state representing the current network phase (e.g., PLAY, CONFIGURATION).
     *
     * @return The current connection protocol.
     */
    default ConnectionProtocol getProtocol()
    {
        return getListener()
            .protocol();
    }

    /**
     * Checks if the connection or listener can accept a given packet payload type.
     *
     * @param type The custom packet payload type to check.
     * @return {@code true} if the type is accepted, {@code false} otherwise.
     */
    boolean canAccept(CustomPacketPayload.Type<?> type);

    /**
     * Terminates the listener connection with the specified reason.
     *
     * @param component The text message describing the disconnection reason.
     */
    default void disconnect(Component component)
    {
        getConnection().disconnect(component);
    }

    /**
     * Terminates the listener connection with the specified complete disconnection details.
     *
     * @param details The detailed object describing the disconnection reason and parameters.
     */
    default void disconnect(DisconnectionDetails details)
    {
        getConnection().disconnect(details);
    }
}
