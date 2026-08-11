package io.pastework.test.common.spell;

import lombok.Getter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class SpellType<Spell extends AbstractSpell>
{
    private final Factory<Spell> supplier;

    @Getter
    private final Identifier identifier;

    @Getter
    private final SpellAttribute defaultAttribute;

    @FunctionalInterface
    public interface Factory<_TSpell extends AbstractSpell>
    {
        _TSpell construct(SpellType<_TSpell> spellType,  Player owner);
    }

    public SpellType(Identifier identifier, SpellAttribute defaultAttribute, Factory<Spell> supplier)
    {
        this.supplier = supplier;
        this.defaultAttribute = defaultAttribute;
        this.identifier = identifier;
    }

    public Spell create(Player owner)
    {
        return supplier.construct(this, owner);
    }
}
