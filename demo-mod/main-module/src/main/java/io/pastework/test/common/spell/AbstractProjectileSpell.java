package io.pastework.test.common.spell;

import lombok.Getter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public abstract class AbstractProjectileSpell extends AbstractSpell
{
    @Getter
    private final Random random = new Random();

    public AbstractProjectileSpell(
        SpellType<? extends AbstractSpell> spellType,
        Player owner
    )
    {
        super(spellType, owner);
    }

    public float getSpreadAngle()
    {
        return 0.0f;
    }

    public int getShootCount()
    {
        return 1;
    }

    public float getShootForceMultiplier()
    {
        return 1.0f;
    }

    protected abstract Projectile constructProjectile(ServerLevel level);

    protected void shootProjectile()
    {
        if(!isClient() && getLevel() instanceof ServerLevel serverLevel)
        {
            int shotAmount = getShootCount();
            var origin = getOwner().getEyePosition();
            float spreadAngle = getSpreadAngle();

            for(var i = 0; i < shotAmount; i++)
            {
                float yaw = getOwner().getXRot() + random.nextFloat(-spreadAngle, spreadAngle);
                float pitch = getOwner().getYHeadRot() + random.nextFloat(-spreadAngle, spreadAngle);

                var velocity =  Vec3.directionFromRotation(yaw, pitch)
                    .normalize()
                    .scale(getShootForceMultiplier());

                final var owner = getOwner();
                var projectile = constructProjectile(serverLevel);

                if(projectile == null)
                {
                    continue;
                }

                projectile.setOwner(owner);
                projectile.setDeltaMovement(velocity);
                projectile.setPos(origin);
                serverLevel.addFreshEntity(projectile);
            }
        }
    }
}
