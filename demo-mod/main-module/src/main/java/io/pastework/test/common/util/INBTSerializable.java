package io.pastework.test.common.util;

import io.pastework.test.common.spell.SpellAttribute;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public interface INBTSerializable
{
    @MustBeInvokedByOverriders
    void serialize(ValueOutput output);

    @MustBeInvokedByOverriders
    void deserialize(ValueInput input);
}
