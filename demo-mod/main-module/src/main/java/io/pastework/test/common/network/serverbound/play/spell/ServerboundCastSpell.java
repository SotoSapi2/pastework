package io.pastework.test.common.network.serverbound.play.spell;

import io.pastework.test.common.PasteworkTest;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public final class ServerboundCastSpell implements CustomPacketPayload
{
    public static ServerboundCastSpell INSTANCE = new ServerboundCastSpell();
    public static final Identifier ID = Identifier.fromNamespaceAndPath(
        PasteworkTest.MOD_ID,
        "play/serverbound_cast_spell"
    );
    public static final CustomPacketPayload.Type<ServerboundCastSpell> TYPE = new CustomPacketPayload.Type<>(
        ID
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundCastSpell> STREAM_CODEC = StreamCodec.unit(
        INSTANCE
    );

    private ServerboundCastSpell()
    {}

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
