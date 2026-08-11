package io.pastework.test.common.spell;

import io.pastework.test.common.registry.ModEntities;
import lombok.Getter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;

public class PotatobreathSpell extends AbstractProjectileSpell
{
    @Getter
    private int shootTickCounter;

    public PotatobreathSpell(SpellType<? extends AbstractSpell> spellType, Player owner)
    {
        super(spellType, owner);
    }

    @Override
    public float getSpreadAngle()
    {
        return 15.0f;
    }

    @Override
    public int getShootCount()
    {
        return getRandom().nextInt(1, 3);
    }

    @Override
    public float getShootForceMultiplier()
    {
        return 2.0f;
    }

    @Override
    public Projectile constructProjectile(ServerLevel level)
    {
        return ModEntities.BURNING_POTATO.getEntry()
            .create(level, EntitySpawnReason.SPAWN_ITEM_USE);
    }

    @Override
    public void onCast()
    {
        if(getOwner() instanceof ServerPlayer serverPlayer &&
            getLevel() instanceof ServerLevel serverLevel
        )
        {
            serverLevel.playSound(
                null,
                getOwner().blockPosition(),
                SoundEvents.ENDER_DRAGON_AMBIENT,
                SoundSource.PLAYERS,
                1.0F,
                getRandom().nextFloat(1.5f, 2)
            );

            serverLevel.sendParticles(
                ParticleTypes.FLAME,
                serverPlayer.getX(),
                serverPlayer.getEyeY(),
                serverPlayer.getZ(),
                25,
                1,
                1,
                1,
                0.1F
            );
        }

        shootTickCounter = 20;
        super.onCast();
    }

    @Override
    public void onTick()
    {
        if(shootTickCounter != 0 && getLevel() instanceof ServerLevel serverLevel)
        {
            shootProjectile();
            shootTickCounter--;
            serverLevel.playSound(
                null,
                getOwner().blockPosition(),
                SoundEvents.EGG_THROW,
                SoundSource.PLAYERS,
                1.0F,
                getRandom().nextFloat(1.2f, 2)
            );

            getOwner().swing(InteractionHand.MAIN_HAND, true);
        }

        super.onTick();
    }
}
