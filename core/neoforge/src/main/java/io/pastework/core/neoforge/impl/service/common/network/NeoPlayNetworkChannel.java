package io.pastework.core.neoforge.impl.service.common.network;

import io.pastework.core.api.common.service.network.IPlayNetworkChannel;
import io.pastework.core.api.common.service.network.PacketInfo;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class NeoPlayNetworkChannel extends NeoAbstractNetworkChannel<RegistryFriendlyByteBuf>
    implements
    IPlayNetworkChannel
{
    public NeoPlayNetworkChannel(
        HandlerThread handlerThread,
        PacketFlow packetFlow,
        NetworkListenerHolder holder
    )
    {
        super(handlerThread, packetFlow, holder);
    }

    @Override
    protected
    <_TPacket extends CustomPacketPayload>
    void handleClientboundPacketInfo(
        PayloadRegistrar registrar,
        PacketInfo<_TPacket, RegistryFriendlyByteBuf> packetInfo
    )
    {
        registrar.playToClient(
            packetInfo.getType(),
            packetInfo.getCodec(),
            (payload, ctx) -> handlePayload(
                packetInfo,
                payload,
                ctx
            )
        );
    }

    @Override
    protected
    <_TPacket extends CustomPacketPayload>
    void handleServerboundPacketInfo(
        PayloadRegistrar registrar,
        PacketInfo<_TPacket, RegistryFriendlyByteBuf> packetInfo
    )
    {
        registrar.playToServer(
            packetInfo.getType(),
            packetInfo.getCodec(),
            (payload, ctx) -> handlePayload(
                packetInfo,
                payload,
                ctx
            )
        );
    }
}
