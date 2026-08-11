package io.pastework.core.neoforge.impl.service.client.network;

import io.pastework.core.api.client.service.network.IPlayServerRemote;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public final class NeoPlayServerRemote implements IPlayServerRemote
{
    @Override
    public void send(CustomPacketPayload payload)
    {
        ClientPacketDistributor.sendToServer(payload);
    }
}
