package io.pastework.test.common.entity;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Optional;

public abstract class HomingProjectile extends Projectile
{
    @Getter
    @Setter
    private double homingSpeed = 2.5;

    @Getter
    @Setter
    private double homingRadius = 8.0;

    @Getter
    @Setter
    private double turnRateFactor = 0.35;

    @Getter
    @Nullable LivingEntity currentHomingTarget;

    public static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> HOME_TARGET_ID = SynchedEntityData
        .defineId(
            HomingProjectile.class,
            EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE
        );

    public HomingProjectile(EntityType<? extends HomingProjectile> entityType, Level level)
    {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder)
    {
        builder.define(HOME_TARGET_ID, Optional.empty());
    }

    @Override
    public void tick()
    {
        super.tick();
        var level = this.level();

        var homeTargetRef = this.getEntityData().get(HOME_TARGET_ID);
        if(level instanceof ServerLevel serverLevel && homeTargetRef.isEmpty())
        {
            @Nullable LivingEntity homeTarget = serverLevel
                .getEntitiesOfClass(
                    LivingEntity.class,
                    this.getBoundingBox().inflate(homingRadius),
                    this::homingFilter
                )
                .stream()
                .min(Comparator.comparingDouble(this::distanceToSqr))
                .orElse(null);

            this.getEntityData().set(
                HOME_TARGET_ID,
                Optional.ofNullable(EntityReference.of(homeTarget))
            );
        }

        if(homeTargetRef.isPresent())
        {
            currentHomingTarget = homeTargetRef.get()
                .getEntity(
                    this.level(),
                    LivingEntity.class
                );
        }

        if(currentHomingTarget != null)
        {
            performHoming(currentHomingTarget);
        }

        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(
            this,
            this::canHitEntity,
            this.getClipType()
        );

        if (hitResult.getType() != HitResult.Type.MISS && this.isAlive())
        {
            this.hitTargetOrDeflectSelf(hitResult);
            return;
        }

        this.setPos(this.position().add(this.getDeltaMovement()));
        this.updateRotation();
    }

    protected void performHoming(@NotNull LivingEntity target)
    {
        Vec3 direction = target.position().add(0, target.getEyeHeight() / 2, 0)
            .subtract(this.position())
            .normalize();

        Vec3 newVel = this.getDeltaMovement().lerp(direction.scale(homingSpeed), turnRateFactor);
        this.setDeltaMovement(newVel);
    }

    protected boolean homingFilter(@NotNull LivingEntity entity)
    {
        return entity != this.getOwner() && entity.isAlive() && !entity.isSpectator();
    }

    protected ClipContext.Block getClipType()
    {
        return ClipContext.Block.COLLIDER;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output)
    {
        super.addAdditionalSaveData(output);
        output.putDouble("HomingSpeed", this.homingSpeed);
        output.putDouble("HomingRadius", this.homingRadius);
        output.putDouble("TurnRateFactor", this.turnRateFactor);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input)
    {
        super.readAdditionalSaveData(input);
        this.homingSpeed = input.getDoubleOr("HomingSpeed", 2.5);
        this.homingRadius = input.getDoubleOr("HomingRadius", 8.0);
        this.turnRateFactor = input.getDoubleOr("TurnRateFactor", 0.35);
    }
}
