package io.pastework.core.neoforge.impl.service.client.network;

import io.pastework.core.api.client.service.network.IClientNetworking;
import io.pastework.core.api.client.service.network.IPlayServerRemote;
import io.pastework.core.api.common.service.network.INetworkRegistry;
import io.pastework.core.api.common.service.network.PacketInfo;
import io.pastework.core.api.common.service.network.IPacketListener;
import io.pastework.core.neoforge.impl.service.common.network.NeoPacketContext;
import io.pastework.core.neoforge.impl.service.common.network.NetworkListenerHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.HandlerThread;
import org.jetbrains.annotations.Nullable;

public final class NeoClientNetworking implements IClientNetworking
{
    private final IPlayServerRemote remote;
    private final INetworkRegistry registry;
    private final NetworkListenerHolder holder;

    public NeoClientNetworking(
        IPlayServerRemote remote,
        INetworkRegistry registry,
        NetworkListenerHolder holder
    )
    {
        this.remote = remote;
        this.registry = registry;
        this.holder = holder;
    }

    @Override
    public IPlayServerRemote getPlayRemote()
    {
        return remote;
    }

    @Override
    public
    <_TPacket extends CustomPacketPayload>
    boolean registerGlobalReceiver(
        CustomPacketPayload.Type<_TPacket> type,
        IPacketListener<_TPacket> listener
    )
    {
        if(registry.forServerPlay().isRegistrationFinalized())
        {
            throw new IllegalStateException("Network registry haven't been finalized.");
        }

        if(holder.hasListener(type))
        {
            return false;
        }

        holder.putListener(type, listener);
        return true;
    }

    @Override
    public void unregisterGlobalReceiver(
        CustomPacketPayload.Type<?> type
    )
    {
        holder.removeListener(type);
    }

    private
    <_TPayload extends CustomPacketPayload, _TBuffer extends FriendlyByteBuf>
    void handlePacketReceiver(
        PacketInfo<_TPayload, _TBuffer> info,
        _TPayload payload,
        IPayloadContext neoCtx,
        HandlerThread threadType
    )
    {
        @Nullable var listener =  holder.getListener(info.getType());

        if(listener != null)
        {
            var ctx = new NeoPacketContext(neoCtx, threadType);
            listener.handlePacket(payload, ctx);
        }
    }
}


