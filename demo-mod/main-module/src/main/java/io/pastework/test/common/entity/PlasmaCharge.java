package io.pastework.test.common.entity;

import io.pastework.test.common.registry.ModEntities;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class PlasmaCharge extends HomingProjectile
{
    @Getter @Setter
    private int lifetime = 20;

    @Getter @Setter
    private float hitDamage = 5.0f;

    public PlasmaCharge(
        EntityType<? extends PlasmaCharge> entityType,
        Level level
    )
    {
        super(entityType, level);
    }

    public PlasmaCharge(Level level, Entity owner)
    {
        super(ModEntities.PLASMA_CHARGE.getEntry(), level);
        this.setOwner(owner);
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {
        super.defineSynchedData(builder);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.level() instanceof ServerLevel serverLevel)
        {
            if (shouldDiedAtThisTick() || (currentHomingTarget != null && !currentHomingTarget.isAlive()))
            {
                kill(serverLevel);
                return;
            }

            if(tickCount % 2 == 0)
            {
                serverLevel.sendParticles(
                    ParticleTypes.END_ROD,
                    this.getX(),
                    this.getY() + 0.25f,
                    this.getZ(),
                    1,
                    0,
                    0,
                    0,
                    0.1F
                );
            }
        }

        this.applyGravity();
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result)
    {
        super.onHitEntity(result);
        if (result.getType() != HitResult.Type.MISS &&
            level() instanceof ServerLevel serverLevel
        )
        {
            DamageSource damageSource = serverLevel.damageSources()
                .magic();

            result.getEntity()
                .hurtServer(serverLevel, damageSource, 5f);
        }
    }

    @Override
    protected void onHit(@NotNull HitResult result)
    {
        super.onHit(result);

        if(level() instanceof ServerLevel serverLevel)
        {
            kill(serverLevel);
        }
    }

    @Override
    public void kill(ServerLevel level)
    {
        super.kill(level);
        level.sendParticles(
            ParticleTypes.EXPLOSION,
            this.getX(),
            this.getY(),
            this.getZ(),
            1,
            0.1,
            0.1,
            0.1,
            1.0F
        );

        this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
    }

    protected boolean shouldDiedAtThisTick()
    {
        if (lifetime <= 0)
        {
            return false;
        }

        return this.tickCount > lifetime;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {
        super.addAdditionalSaveData(output);
        output.putInt("Lifetime", this.lifetime);
        output.putFloat("HitDamage", this.hitDamage);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
        super.readAdditionalSaveData(input);
        this.lifetime = input.getIntOr("Lifetime", 20);
        this.hitDamage = input.getIntOr("HitDamage", 5);
    }
}
