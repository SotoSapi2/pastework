package io.pastework.test.common.spell;

import io.pastework.test.common.entity.PlasmaCharge;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;

public class PlasmaburstSpell extends AbstractProjectileSpell
{
    public PlasmaburstSpell(SpellType<? extends AbstractSpell> spellType, Player owner)
    {
        super(spellType, owner);
    }

    @Override
    public float getSpreadAngle()
    {
        return 15.0f;
    }

    @Override
    public float getShootForceMultiplier()
    {
        return 2;
    }

    @Override
    public int getShootCount()
    {
        return getRandom().nextInt(2, 5);
    }

    @Override
    public Projectile constructProjectile(ServerLevel level)
    {
        return new PlasmaCharge(level, getOwner());
    }

    @Override
    public void onCast()
    {
        if(getLevel() instanceof ServerLevel serverLevel)
        {
            shootProjectile();
            serverLevel.playSound(
                null,
                getOwner().blockPosition(),
                SoundEvents.SHULKER_SHOOT,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
            );
        }

        super.onCast();
    }
}
