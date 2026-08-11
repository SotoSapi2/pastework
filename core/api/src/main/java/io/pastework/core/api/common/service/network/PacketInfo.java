package io.pastework.core.api.common.service.network;

import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@Getter
public final class PacketInfo<TPayload extends CustomPacketPayload, TBuffer extends FriendlyByteBuf>
{
    private final PacketFlow packetFlow;
    private final CustomPacketPayload.Type<TPayload> type;
    private final StreamCodec<? super TBuffer, TPayload> codec;

    public PacketInfo(
        PacketFlow packetFlow,
        CustomPacketPayload.Type<TPayload> type,
        StreamCodec<? super TBuffer, TPayload> codec
    )
    {
        this.packetFlow = packetFlow;
        this.type = type;
        this.codec = codec;
    }
}
