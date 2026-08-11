package io.pastework.core.base.common.impl.service.network;

import io.pastework.core.api.common.service.network.INetworkChannel;
import io.pastework.core.api.common.service.network.PacketInfo;
import io.pastework.core.api.exception.RegistryException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractNetworkChannel<TBuffer extends FriendlyByteBuf> implements INetworkChannel<TBuffer>
{
    private final PacketFlow packetFlow;
    private volatile boolean isRegistrationFinalized;
    private final Map<Identifier, PacketInfo<? extends CustomPacketPayload, TBuffer>> registryMap = new HashMap<>();

    public AbstractNetworkChannel(PacketFlow packetFlow)
    {
        this.packetFlow = packetFlow;
    }

    @Override
    public boolean isRegistrationFinalized()
    {
        return isRegistrationFinalized;
    }

    @Override
    public
    <_TPayload extends CustomPacketPayload>
    void enqueuePacket(
        CustomPacketPayload.Type<_TPayload> type,
        StreamCodec<TBuffer, _TPayload> codec
    ) throws RegistryException
    {
        var packetInfo = new PacketInfo<>(
            getChannelFlow(),
            type,
            codec
        );

        if(isRegistrationFinalized)
        {
            throw new RegistryException(
                "Couldn't register new packet after the channel had been finalized for registration."
            );
        }

        synchronized (registryMap)
        {
            if(isPacketRegistered(packetInfo.getType()))
            {
                throw new RegistryException(String.format(
                    "Packet '%s' is already registered as %s packet.",
                    packetInfo.getType(),
                    packetInfo.getPacketFlow()
                ));
            }

            registryMap.put(packetInfo.getType().id(), packetInfo);
        }
    }

    @Override
    public Collection<PacketInfo<? extends CustomPacketPayload, TBuffer>> getRegisteredPackets()
    {
        return Collections.unmodifiableCollection(registryMap.values());
    }

    @Override
    public PacketFlow getChannelFlow()
    {
        return packetFlow;
    }

    @Override
    public boolean isPacketRegistered(CustomPacketPayload.@NonNull Type<?> type)
    {
        return registryMap.containsKey(type.id());
    }

    protected void finalizeRegistration()
    {
        isRegistrationFinalized = true;
    }

    protected void throwIfPacketOppositeFlow(PacketInfo<?, ?> info)
    {
        if(info.getPacketFlow() != getChannelFlow())
        {
            throw new IllegalStateException(String.format(
                "'%s' can only accept %s packet flow. But '%s' wanted the opposite flow.",
                this,
                getChannelFlow(),
                info
            ));
        }
    }
}
