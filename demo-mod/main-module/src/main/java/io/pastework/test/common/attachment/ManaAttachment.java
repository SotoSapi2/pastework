package io.pastework.test.common.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import lombok.With;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

@With
public record ManaAttachment(int amount, int regenRate, int maxAmount)
{
    public static final Codec<ManaAttachment> CODEC = RecordCodecBuilder.create(it ->
        it.group(
            Codec.INT.fieldOf("amount").forGetter(ManaAttachment::amount),
            Codec.INT.fieldOf("regenRate").forGetter(ManaAttachment::regenRate),
            Codec.INT.fieldOf("maxAmount").forGetter(ManaAttachment::maxAmount)
        ).apply(
            it,
            ManaAttachment::new
        )
    );

    public static ManaAttachment createDefault()
    {
        return new ManaAttachment(100, 1, 100);
    }

    public static final StreamCodec<ByteBuf, ManaAttachment> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, ManaAttachment::amount,
        ByteBufCodecs.INT, ManaAttachment::regenRate,
        ByteBufCodecs.INT, ManaAttachment::maxAmount,
        ManaAttachment::new
    );

    public ManaAttachment consume(int amount)
    {
        int newAmount = Math.max(0, this.amount - amount);
        return this.withAmount(newAmount);
    }

    public ManaAttachment replenish(int amount)
    {
        int newAmount = Math.min(this.maxAmount(), this.amount + amount);
        return this.withAmount(newAmount);
    }

    public ManaAttachment replenishByRegenRate()
    {
        return replenish(regenRate);
    }
}
