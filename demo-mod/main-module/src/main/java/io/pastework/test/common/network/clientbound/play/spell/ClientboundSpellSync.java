package io.pastework.test.common.network.clientbound.play.spell;

import io.pastework.test.common.PasteworkTest;
import io.pastework.test.common.spell.SpellSyncRecord;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Collection;

public record ClientboundSpellSync(
    int spellSelectIndex,
    boolean requireCacheCleanUp,
    Collection<SpellSyncRecord> spellRecords
) implements CustomPacketPayload
{
    public static final Identifier ID = Identifier.fromNamespaceAndPath(
        PasteworkTest.MOD_ID,
        "play/clientbound_spell_sync"
    );

    public static final Type<ClientboundSpellSync> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSpellSync> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        ClientboundSpellSync::spellSelectIndex,
        ByteBufCodecs.BOOL,
        ClientboundSpellSync::requireCacheCleanUp,
        ByteBufCodecs.collection(ArrayList::new, SpellSyncRecord.STREAM_CODEC),
        ClientboundSpellSync::spellRecords,
        ClientboundSpellSync::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
