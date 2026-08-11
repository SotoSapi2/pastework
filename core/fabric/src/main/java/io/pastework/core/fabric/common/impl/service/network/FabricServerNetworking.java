package io.pastework.core.fabric.common.impl.service.network;

import io.pastework.core.api.common.service.network.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class FabricServerNetworking implements IServerNetworking
{
    private final IPlayClientRemote remote;
    private final INetworkRegistry registry;

    public FabricServerNetworking(IPlayClientRemote remote, INetworkRegistry registry)
    {
        this.remote = remote;
        this.registry = registry;
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
        if(!registry.forServerPlay().isRegistrationFinalized())
        {
            throw new IllegalStateException("Network registry haven't been finalized.");
        }

        return ServerPlayNetworking.registerGlobalReceiver(
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
        ServerPlayNetworking.unregisterGlobalReceiver(type.id());
    }

    private
    <_TPacket extends CustomPacketPayload>
    void handlePacketReceiver(
        _TPacket packet,
        ServerPlayNetworking.Context ctx,
        IPacketListener<_TPacket> listener
    )
    {
        var packetCtx = FabricServerPacketContext.fromPlayContext(
            NetworkThreadType.MAIN,
            ctx
        );

        listener.handlePacket(packet, packetCtx);
    }
}
