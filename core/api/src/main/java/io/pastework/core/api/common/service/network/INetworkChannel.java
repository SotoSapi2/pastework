package io.pastework.core.api.common.service.network;

import io.pastework.core.api.exception.RegistryException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Collection;

/**
 * Interface representing a network channel for registering and managing custom packets.
 *
 * @param <TBuffer> The type of buffer used for packet serialization and deserialization.
 */
public interface INetworkChannel<TBuffer extends FriendlyByteBuf>
{
    /**
     * Checks if the packet registration phase for this channel has been finalized.
     *
     * @return {@code true} if registration is final and no more packets can be registered, {@code false} otherwise.
     */
    boolean isRegistrationFinalized();

    /**
     * Enqueues a custom packet payload for registration.
     *
     * @param type  The type of the custom packet payload.
     * @param codec The codec used to serialize and deserialize the packet.
     * @param <_TPayload> The payload type.
     * @throws RegistryException If the packet cannot be registered (e.g., if registration is finalized).
     */
    <_TPayload extends CustomPacketPayload>
    void enqueuePacket(
        CustomPacketPayload.Type<_TPayload> type,
        StreamCodec<TBuffer, _TPayload> codec
    ) throws RegistryException;

    /**
     * Gets a collection of all registered packets in this channel.
     *
     * @return A collection containing the packet information for each registered packet.
     */
    Collection<PacketInfo<? extends CustomPacketPayload, TBuffer>> getRegisteredPackets();

    /**
     * Checks if a packet of the given type is already registered in this channel.
     *
     * @param type The type of the custom packet payload to check.
     * @return {@code true} if the packet is registered, {@code false} otherwise.
     */
    boolean isPacketRegistered(CustomPacketPayload.Type<?> type);

    /**
     * Gets the packet flow direction for this channel (clientbound or serverbound).
     *
     * @return The channel's packet flow direction.
     */
    PacketFlow getChannelFlow();
}
