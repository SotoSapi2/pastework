package io.pastework.test.common.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@With
@Getter
@Builder(builderClassName = "Builder")
public final class SpellAttribute
{
    public static final SpellAttribute DEFAULT = SpellAttribute
        .builder()
        .build();

    public static final Codec<SpellAttribute> CODEC = RecordCodecBuilder.create(it ->
        it.group(
            Codec.INT.fieldOf("useCooldown").forGetter(SpellAttribute::getUseTickCooldown),
            Codec.INT.fieldOf("useCost").forGetter(SpellAttribute::getUseCost),
            Codec.FLOAT.fieldOf("damageMultiplier").forGetter(SpellAttribute::getDamageMultiplier)
        ).apply(
            it,
            SpellAttribute::new
        )
    );

    @lombok.Builder.Default
    private int useTickCooldown = 20;

    @lombok.Builder.Default
    private int useCost = 10;

    @lombok.Builder.Default
    private float damageMultiplier = 1;
}
