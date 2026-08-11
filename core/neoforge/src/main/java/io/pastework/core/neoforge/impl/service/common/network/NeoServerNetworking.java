package io.pastework.core.neoforge.impl.service.common.network;

import io.pastework.core.api.common.service.network.INetworkRegistry;
import io.pastework.core.api.common.service.network.IPlayClientRemote;
import io.pastework.core.api.common.service.network.IServerNetworking;
import io.pastework.core.api.common.service.network.IPacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class NeoServerNetworking implements IServerNetworking
{
    private final IPlayClientRemote remote;
    private final NetworkListenerHolder holder;
    private final INetworkRegistry registry;

    public NeoServerNetworking(
        IPlayClientRemote remote,
        INetworkRegistry registry,
        NetworkListenerHolder holder
    )
    {
        this.remote = remote;
        this.registry = registry;
        this.holder = holder;
    }

    @Override
    public IPlayClientRemote getPlayRemote()
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
}
