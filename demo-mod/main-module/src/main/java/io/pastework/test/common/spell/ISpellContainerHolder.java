package io.pastework.test.common.spell;

import net.minecraft.server.level.ServerPlayer;

import java.util.List;

/**
 * Extension interface for {@link ServerPlayer}.
 */
public interface ISpellContainerHolder
{
    int getSelectedSpellIndex();

    void setSelectedSpellIndex(int index);

    List<AbstractSpell> getSpellList();

    default boolean isSpellIndexValid(int index)
    {
        return index >= 0 && index < getSpellList().size();
    }

    default boolean isSelectedSpellIndexValid()
    {
        return isSpellIndexValid(getSelectedSpellIndex());
    }

    default boolean hasNoSpell()
    {
        return getSpellList().isEmpty();
    }

    void addSpell(SpellType<?> spellType);

    default void removeSpell(int index)
    {
        getSpellList().remove(index);
    }

    default void removeSpell(AbstractSpell spell)
    {
        getSpellList().remove(spell);
    }

    default int getSpellCount()
    {
        return getSpellList().size();
    }

    default AbstractSpell getSelectedSpell()
    {
        if(!isSelectedSpellIndexValid())
        {
            throw new IndexOutOfBoundsException();
        }

        return getSpellList().get(getSelectedSpellIndex());
    }
}
