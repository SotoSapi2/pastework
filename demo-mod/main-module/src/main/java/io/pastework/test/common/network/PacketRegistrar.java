package io.pastework.test.common.network;

import io.pastework.core.api.common.service.network.INetworkRegistry;
import io.pastework.core.api.common.service.network.IPlayNetworkChannel;
import io.pastework.test.common.network.clientbound.play.spell.ClientboundSpellSync;
import io.pastework.test.common.network.serverbound.play.spell.ServerboundCastSpell;
import io.pastework.test.common.network.serverbound.play.spell.ServerboundSelectSpell;
import io.pastework.test.common.network.serverbound.play.spell.ServerboundSyncSpellRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PacketRegistrar
{
    private static final INetworkRegistry NETWORK_REGISTRY = INetworkRegistry.getService();
    private static final IPlayNetworkChannel SERVER_PLAY_CHANNEL = NETWORK_REGISTRY.forServerPlay();
    private static final IPlayNetworkChannel CLIENT_PLAY_CHANNEL = NETWORK_REGISTRY.forClientPlay();

    public static void initialize()
    {
        registerServerPlay();
        registerClientPlay();
    }

    private static void registerServerPlay()
    {
        SERVER_PLAY_CHANNEL.enqueuePacket(
            ServerboundCastSpell.TYPE,
            ServerboundCastSpell.STREAM_CODEC
        );

        SERVER_PLAY_CHANNEL.enqueuePacket(
            ServerboundSelectSpell.TYPE,
            ServerboundSelectSpell.STREAM_CODEC
        );

        SERVER_PLAY_CHANNEL.enqueuePacket(
            ServerboundSyncSpellRequest.TYPE,
            ServerboundSyncSpellRequest.STREAM_CODEC
        );
    }

    private static void registerClientPlay()
    {
        CLIENT_PLAY_CHANNEL.enqueuePacket(
            ClientboundSpellSync.TYPE,
            ClientboundSpellSync.STREAM_CODEC
        );
    }
}
