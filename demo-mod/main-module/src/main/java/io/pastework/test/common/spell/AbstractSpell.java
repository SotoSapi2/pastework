package io.pastework.test.common.spell;

import io.pastework.core.api.common.service.attachment.IAttachableExtension;
import io.pastework.test.common.attachment.ManaAttachment;
import io.pastework.test.common.registry.Attachments;
import io.pastework.test.common.util.INBTSerializable;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

public abstract class AbstractSpell implements INBTSerializable
{

    @Getter
    private final Player owner;

    @Getter
    private SpellAttribute attribute;

    @Getter
    @Setter
    private int useCooldownTimer;

    @Getter
    private final SpellType<? extends AbstractSpell> spellType;

    public AbstractSpell(SpellType<? extends AbstractSpell> spellType, Player owner)
    {
        this.spellType = spellType;
        this.owner = owner;
        this.attribute = spellType.getDefaultAttribute();
        refreshState();
    }

    public void setAttribute(SpellAttribute attribute)
    {
        this.attribute = attribute;
        refreshState();
    }

    public Identifier getIdentifier()
    {
        return spellType.getIdentifier();
    }

    public Level getLevel()
    {
        return owner.level();
    }

    public boolean isClient()
    {
        return getLevel().isClientSide();
    }

    public boolean isOnCooldown()
    {
        return useCooldownTimer > 0;
    }

    public boolean isOwnerHaveEnoughMana()
    {
        if (((IAttachableExtension) getOwner()).hasAttachment(Attachments.MANA))
        {
            var mana = (ManaAttachment) ((IAttachableExtension) getOwner()).getOrThrow(Attachments.MANA);
            return mana.amount() > attribute.getUseCost();
        }

        return false;
    }

    public boolean isSpellCanBeUsed()
    {
        return !isOnCooldown() && isOwnerHaveEnoughMana();
    }

    public void onSelect()
    {
    }

    @MustBeInvokedByOverriders
    public void onCast()
    {
        if (getOwner() instanceof ServerPlayer serverPlayer)
        {
            ((IAttachableExtension) serverPlayer).editAttachment(
                Attachments.MANA,
                it -> ((ManaAttachment) it).consume(attribute.getUseCost())
            );
        }

        useCooldownTimer = attribute.getUseTickCooldown();
    }

    @MustBeInvokedByOverriders
    public void onTick()
    {
        if (useCooldownTimer > 0)
        {
            useCooldownTimer--;
        }
    }

    public void refreshState()
    {
        useCooldownTimer = attribute.getUseTickCooldown();
    }

    @Override
    @MustBeInvokedByOverriders
    public void serialize(ValueOutput output)
    {
        output.putInt("UseCooldownTimer", useCooldownTimer);
        output.store("SpellAttribute", SpellAttribute.CODEC, attribute);
    }

    @Override
    @MustBeInvokedByOverriders
    public void deserialize(ValueInput input)
    {
        attribute = input.read("SpellAttribute", SpellAttribute.CODEC).orElseThrow();
        useCooldownTimer = input.getIntOr("UseCooldownTimer", attribute.getUseTickCooldown());
    }
}
