package io.pastework.core.fabric.client.impl.service.network;

import io.pastework.core.api.client.service.network.IPlayServerRemote;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class FabricPlayServerRemote implements IPlayServerRemote
{
    @Override
    public void send(CustomPacketPayload payload)
    {
        ClientPlayNetworking.send(payload);
    }
}
