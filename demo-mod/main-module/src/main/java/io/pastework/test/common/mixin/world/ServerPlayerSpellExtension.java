package io.pastework.test.common.mixin.world;

import io.pastework.core.api.common.service.registry.ICommonRegistry;
import io.pastework.test.common.TagConstants;
import io.pastework.test.common.registry.Spells;
import io.pastework.test.common.spell.AbstractSpell;
import io.pastework.test.common.spell.ISpellContainerHolder;
import io.pastework.test.common.spell.SpellType;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Mixin(ServerPlayer.class)
public class ServerPlayerSpellExtension implements ISpellContainerHolder
{
    @Unique
    private final List<AbstractSpell> pastework$spellList = new ArrayList<>();

    @Unique
    private int pastework$selectedSpellIndex = 0;

    @Override
    public int getSelectedSpellIndex()
    {
        return pastework$selectedSpellIndex;
    }

    @Override
    public void setSelectedSpellIndex(int index)
    {
        pastework$selectedSpellIndex = index;
    }

    @Override
    public List<AbstractSpell> getSpellList()
    {
        return pastework$spellList;
    }

    @Override
    public void addSpell(SpellType<?> spellType)
    {
        var player = (Player) (Object) this;
        var spell = spellType.create(player);

        pastework$spellList.add(spell);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(ValueOutput output, CallbackInfo ci)
    {
        output.putInt(TagConstants.TAG_SELECTED_SPELL, pastework$selectedSpellIndex);
        var spellListOutput = output.childrenList(TagConstants.TAG_SPELL_CONTAINER);

        for (AbstractSpell spell : pastework$spellList)
        {
            var spellOutput = spellListOutput.addChild();
            spellOutput.putString(TagConstants.TAG_SPELL_ID, spell.getIdentifier().toString());
            spell.serialize(spellOutput);
        }

        if (spellListOutput.isEmpty())
        {
            spellListOutput.discardLast();
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(ValueInput input, CallbackInfo ci)
    {
        var registry = ICommonRegistry.getService()
            .requestNativeRegistry(Spells.SPELL_REGISTRY_KEY);

        var spellListInput = input.childrenList(TagConstants.TAG_SPELL_CONTAINER);
        spellListInput.ifPresent(it ->
        {
            var iterator = it.stream().iterator();
            while (iterator.hasNext())
            {
                var spellData = iterator.next();
                pastework$loadSpellData(registry.value(), spellData);
            }
        });
    }

    @Unique
    private void pastework$loadSpellData(Registry<SpellType<?>> spellTypeRegistry, ValueInput input)
    {
        var player = (Player) (Object) this;
        var idStr = input.getString(TagConstants.TAG_SPELL_ID)
            .orElseThrow(() -> new NoSuchElementException(String.format(
                "Couldn't find '%s' tag",
                TagConstants.TAG_SPELL_ID
            )));

        var id = Identifier.parse(idStr);
        var spellType = spellTypeRegistry.get(id)
            .orElseThrow(() -> new NoSuchElementException(String.format(
                "Couldn't find spell '%s' registry",
                id
            )))
            .value();

        var spell = spellType.create(player);
        spell.deserialize(input);

        pastework$spellList.add(spell);
    }
}
