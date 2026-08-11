package io.pastework.core.neoforge.impl.service.common.network;

import io.pastework.core.api.common.service.network.IPacketContext;
import io.pastework.core.base.common.impl.service.network.AbstractNetworkChannel;
import io.pastework.core.api.common.service.network.PacketInfo;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.Nullable;

public abstract class NeoAbstractNetworkChannel<TBuffer extends FriendlyByteBuf> extends AbstractNetworkChannel<TBuffer>
{
    @Getter
    private final HandlerThread handlerThread;
    private final NetworkListenerHolder holder;

    public NeoAbstractNetworkChannel(
        HandlerThread handlerThread,
        PacketFlow packetFlow,
        NetworkListenerHolder holder
    )
    {
        super(packetFlow);
        this.handlerThread = handlerThread;
        this.holder = holder;
    }

    public final void onNetworkRegistry(PayloadRegistrar registrar)
    {
        registrar.executesOn(handlerThread);

        for(var packetInfo : getRegisteredPackets())
        {
            registerPacket(registrar, packetInfo);
        }

        finalizeRegistration();
    }

    protected abstract
    <_TPacket extends CustomPacketPayload>
    void handleClientboundPacketInfo(PayloadRegistrar registrar, PacketInfo<_TPacket, TBuffer> packetInfo);

    protected abstract
    <_TPacket extends CustomPacketPayload>
    void handleServerboundPacketInfo(PayloadRegistrar registrar, PacketInfo<_TPacket, TBuffer> packetInfo);

    private
    <_TPacket extends CustomPacketPayload>
    void registerPacket(
        PayloadRegistrar registrar,
        PacketInfo<_TPacket, TBuffer> packetInfo
    )
    {
        throwIfPacketOppositeFlow(packetInfo);

        switch (packetInfo.getPacketFlow())
        {
            case CLIENTBOUND -> handleClientboundPacketInfo(registrar, packetInfo);
            case SERVERBOUND -> handleServerboundPacketInfo(registrar, packetInfo);
            case null, default -> throw new UnsupportedOperationException(
                "No implementation for network bound type: " + packetInfo.getPacketFlow()
            );
        }
    }

    protected final
    <_TPacket extends CustomPacketPayload>
    void handlePayload(
        PacketInfo<_TPacket, TBuffer> packetInfo,
        _TPacket payload,
        IPayloadContext ctx
    )
    {
        throwIfPacketOppositeFlow(packetInfo);

        IPacketContext packetContext = new NeoPacketContext(
            ctx,
            HandlerThread.NETWORK
        );

        @Nullable var listener = holder.getListener(packetInfo.getType());

        if(listener != null)
        {
            listener.handlePacket(payload, packetContext);
        }
    }
}
