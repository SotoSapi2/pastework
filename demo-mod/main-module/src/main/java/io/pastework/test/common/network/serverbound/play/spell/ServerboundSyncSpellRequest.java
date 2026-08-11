package io.pastework.test.common.network.serverbound.play.spell;

import io.pastework.test.common.PasteworkTest;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public final class ServerboundSyncSpellRequest implements CustomPacketPayload
{
    public static ServerboundSyncSpellRequest INSTANCE = new ServerboundSyncSpellRequest();
    public static final Identifier ID = Identifier.fromNamespaceAndPath(
        PasteworkTest.MOD_ID,
        "play/serverbound_spell_sync_request"
    );
    public static final CustomPacketPayload.Type<ServerboundSyncSpellRequest> TYPE = new CustomPacketPayload.Type<>(
        ID
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSyncSpellRequest> STREAM_CODEC = StreamCodec.unit(
        INSTANCE
    );

    private ServerboundSyncSpellRequest()
    {}

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return null;
    }
}
