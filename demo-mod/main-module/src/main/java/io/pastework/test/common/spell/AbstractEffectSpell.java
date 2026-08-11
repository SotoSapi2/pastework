package io.pastework.test.common.spell;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractEffectSpell extends AbstractSpell
{
    public AbstractEffectSpell(SpellType<? extends AbstractSpell> spellType, Player owner)
    {
        super(spellType, owner);
    }

    @Override
    public void onCast()
    {
        if(getLevel() instanceof ServerLevel serverLevel &&
            getOwner() instanceof ServerPlayer serverPlayer
        )
        {
            MobEffectInstance effectInstance = getEffectSpell();
            serverPlayer.addEffect(effectInstance);
        }

        super.onCast();
    }

    protected abstract MobEffectInstance getEffectSpell();
}
