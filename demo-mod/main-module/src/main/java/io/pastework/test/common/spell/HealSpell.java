package io.pastework.test.common.spell;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class HealSpell extends AbstractEffectSpell
{
    public HealSpell(SpellType<? extends AbstractSpell> spellType, Player owner)
    {
        super(spellType, owner);
    }

    @Override
    public void onCast()
    {
        if (getLevel() instanceof ServerLevel serverLevel &&
            getOwner() instanceof ServerPlayer serverPlayer
        )
        {
            serverLevel.playSound(
                null,
                serverPlayer.blockPosition(),
                SoundEvents.TOTEM_USE,
                SoundSource.PLAYERS,
                1,
                1.5f
            );

            serverLevel.sendParticles(
                ParticleTypes.HEART,
                serverPlayer.getX(),
                serverPlayer.getEyeY(),
                serverPlayer.getZ(),
                10,
                1,
                1,
                1,
                1.0F
            );
        }

        super.onCast();
    }

    @Override
    protected MobEffectInstance getEffectSpell()
    {
        return new MobEffectInstance(MobEffects.INSTANT_HEALTH, 3, 5);
    }
}
