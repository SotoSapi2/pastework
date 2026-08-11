package io.pastework.test.common.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class BurningPotato extends ThrowableItemProjectile
{
    public BurningPotato(
        EntityType<? extends ThrowableItemProjectile> entityType,
        Level level
    )
    {
        super(entityType, level);
    }

    @Override
    protected Item getDefaultItem()
    {
        return Items.POTATO;
    }

    @Override
    public void tick()
    {
        if(level() instanceof ServerLevel serverLevel && tickCount % 3 == 0)
        {
            serverLevel.sendParticles(
                ParticleTypes.FLAME,
                this.getX(),
                this.getY(),
                this.getZ(),
                1,
                0,
                0,
                0,
                0.1F
            );
        }

        igniteForTicks(20);
        super.tick();
    }

    @Override
    protected boolean canHitEntity(Entity target)
    {
        return target instanceof LivingEntity && target != getOwner();
    }

    @Override
    protected void onHitEntity(EntityHitResult result)
    {
        if(result.getEntity() != getOwner() &&
            level() instanceof ServerLevel serverLevel
        )
        {
            var entity = result.getEntity();
            DamageSource damageSource = serverLevel.damageSources()
                .generic();

            entity.lavaIgnite();
            entity.invulnerableTime = 0;
            entity.hurtServer(serverLevel, damageSource, 2.5f);
        }

        super.onHitEntity(result);
    }

    @Override
    protected void onHit(HitResult result)
    {
        this.discard();
        super.onHit(result);
    }
}
