package io.pastework.core.neoforge.impl.service.common.network;

import io.pastework.core.api.common.service.network.IPacketListener;
import lombok.Getter;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class NetworkListenerHolder
{
    @Getter
    private final PacketFlow packetFlow;
    private final Map<CustomPacketPayload.Type<?>, IPacketListener<?>> listenerMap = new HashMap<>();

    public NetworkListenerHolder(PacketFlow packetFlow)
    {
        this.packetFlow = packetFlow;
    }

    @SuppressWarnings("unchecked")
    public
    <_TPacket extends CustomPacketPayload>
    @Nullable IPacketListener<_TPacket> getListener(CustomPacketPayload.Type<_TPacket> type)
    {
        return (IPacketListener<_TPacket>) listenerMap.get(type);
    }

    public
    <_TPacket extends CustomPacketPayload>
    void putListener(CustomPacketPayload.Type<_TPacket> type, IPacketListener<_TPacket> listener)
    {
        listenerMap.put(type, listener);
    }

    public boolean hasListener(CustomPacketPayload.Type<?> type)
    {
        return listenerMap.containsKey(type);
    }

    public void removeListener(CustomPacketPayload.Type<?> type)
    {
        listenerMap.remove(type);
    }
}
