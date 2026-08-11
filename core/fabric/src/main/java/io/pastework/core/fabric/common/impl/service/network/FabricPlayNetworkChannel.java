package io.pastework.core.fabric.common.impl.service.network;

import io.pastework.core.api.common.service.network.IPlayNetworkChannel;
import io.pastework.core.api.common.service.network.PacketInfo;
import io.pastework.core.base.common.impl.service.network.AbstractNetworkChannel;
import io.pastework.core.fabric.common.impl.service.IFabricRegistrable;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class FabricPlayNetworkChannel extends AbstractNetworkChannel<RegistryFriendlyByteBuf>
    implements
    IPlayNetworkChannel,
    IFabricRegistrable
{
    public FabricPlayNetworkChannel(PacketFlow packetFlow)
    {
        super(packetFlow);
    }

    @Override
    public boolean isRegistrationFinalized()
    {
        return super.isRegistrationFinalized();
    }

    @Override
    public void processRegistration()
    {
        if(isRegistrationFinalized())
        {
            throw new IllegalStateException();
        }

        for(var packetInfo : getRegisteredPackets())
        {
            registerPacket(packetInfo);
        }

        finalizeRegistration();
    }

    private
    <_TPayload extends CustomPacketPayload>
    void registerPacket(
        PacketInfo<_TPayload, RegistryFriendlyByteBuf> packetInfo
    )
    {
        throwIfPacketOppositeFlow(packetInfo);

        switch (packetInfo.getPacketFlow())
        {
            case SERVERBOUND ->
            {
                PayloadTypeRegistry.playC2S()
                    .register(packetInfo.getType(), packetInfo.getCodec());
            }

            case CLIENTBOUND ->
            {
                PayloadTypeRegistry.playS2C()
                    .register(packetInfo.getType(), packetInfo.getCodec());
            }

            case null, default -> throw new UnsupportedOperationException(
                "No implementation for network bound type: " + packetInfo.getPacketFlow()
            );
        }
    }
}
