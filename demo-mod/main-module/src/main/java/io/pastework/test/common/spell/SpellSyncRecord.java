package io.pastework.test.common.spell;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public record SpellSyncRecord(
    Identifier identifier,
    CompoundTag tag
)
{
    public static StreamCodec<ByteBuf, SpellSyncRecord> STREAM_CODEC = StreamCodec.composite(
        Identifier.STREAM_CODEC,
        SpellSyncRecord::identifier,
        ByteBufCodecs.COMPOUND_TAG,
        SpellSyncRecord::tag,
        SpellSyncRecord::new
    );

    public static Collection<SpellSyncRecord> fromHolder(ProblemReporter.Collector reporter, ISpellContainerHolder holder)
    {
        List<SpellSyncRecord> out = new ArrayList<>();

        for(AbstractSpell spell : holder.getSpellList())
        {
            var output = TagValueOutput.createWithoutContext(reporter);
            spell.serialize(output);

            var record = new SpellSyncRecord(
                spell.getIdentifier(),
                output.buildResult()
            );

            out.add(record);
        }

        return Collections.unmodifiableList(out);
    }

    public void syncSpell(ProblemReporter.Collector reporter, AbstractSpell spell)
    {
        ValueInput input = TagValueInput.create(
            reporter,
            spell.getOwner().level().registryAccess(),
            tag
        );

        spell.deserialize(input);
    }

    public AbstractSpell constructSpell(
        ProblemReporter.Collector reporter,
        Registry<SpellType<?>> registry,
        Player owner
    )
    {
        AbstractSpell spell = registry.get(identifier)
            .orElseThrow()
            .value()
            .create(owner);

        ValueInput input = TagValueInput.create(
            reporter,
            spell.getOwner().level().registryAccess(),
            tag
        );

        spell.deserialize(input);
        return spell;
    }
}
