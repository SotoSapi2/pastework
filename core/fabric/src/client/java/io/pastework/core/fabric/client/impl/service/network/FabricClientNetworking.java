package io.pastework.core.fabric.client.impl.service.network;

import io.pastework.core.api.client.service.network.IClientNetworking;
import io.pastework.core.api.client.service.network.IPlayServerRemote;
import io.pastework.core.api.common.service.network.INetworkRegistry;
import io.pastework.core.api.common.service.network.IPacketListener;
import io.pastework.core.api.common.service.network.NetworkThreadType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class FabricClientNetworking implements IClientNetworking
{
    private final IPlayServerRemote serverRemote;
    private final INetworkRegistry registry;

    public FabricClientNetworking(IPlayServerRemote serverRemote, INetworkRegistry registry)
    {
        this.serverRemote = serverRemote;
        this.registry = registry;
    }

    @Override
    public IPlayServerRemote getPlayRemote()
    {
        return serverRemote;
    }

    @Override
    public
    <_TPacket extends CustomPacketPayload>
    boolean registerGlobalReceiver(
        CustomPacketPayload.Type<_TPacket> type,
        IPacketListener<_TPacket> listener
    )
    {
        if(!registry.forServerPlay().isRegistrationFinalized())
        {
            throw new IllegalStateException("Network registry haven't been finalized.");
        }

        return ClientPlayNetworking.registerGlobalReceiver(
            type,
            (packet, ctx) -> handlePacketReceiver(
                packet,
                ctx,
                listener
            )
        );
    }

    @Override
    public void unregisterGlobalReceiver(CustomPacketPayload.Type<?> type)
    {
        ClientPlayNetworking.unregisterReceiver(type.id());
    }

    private
    <_TPacket extends CustomPacketPayload>
    void handlePacketReceiver(
        _TPacket packet,
        ClientPlayNetworking.Context ctx,
        IPacketListener<_TPacket> listener
    )
    {
        var packetCtx = FabricClientNetworkContext.fromPlayContext(
            NetworkThreadType.MAIN,
            ctx
        );

        listener.handlePacket(packet, packetCtx);
    }
}
