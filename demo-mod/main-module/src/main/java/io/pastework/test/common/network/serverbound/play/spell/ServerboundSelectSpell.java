package io.pastework.test.common.network.serverbound.play.spell;

import io.pastework.test.common.PasteworkTest;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ServerboundSelectSpell(int spellIndex) implements CustomPacketPayload
{
    public static final Identifier ID = Identifier.fromNamespaceAndPath(
        PasteworkTest.MOD_ID,
        "play/serverbound_select_spell"
    );
    public static final CustomPacketPayload.Type<ServerboundSelectSpell> TYPE = new CustomPacketPayload.Type<>(
        ID
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSelectSpell> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        ServerboundSelectSpell::spellIndex,
        ServerboundSelectSpell::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
