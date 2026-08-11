package io.pastework.core.api.common.event.world;

import io.pastework.core.api.common.event.Event1;
import io.pastework.core.api.common.event.ICancellableEventContext;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;

/**
 * Contains events for some occurrences that {@link LivingEntity} made (e.g. Died, Hurt).
 */
@ApiStatus.NonExtendable
public interface LivingEntityEvent
{
    /**
     * Fired when an entity died.
     * <p>
     * This event runs on both Client and Server logical side.
     */
    Event1<DiedContext> DIED = new Event1<>();

    /**
     * Fired before an entity processes damage when hurt.
     * <p>
     * This event runs on both Client and Server logical side and can be cancelled.
     */
    Event1<HurtContext> HURT = new Event1<>();

    /**
     * Fired after an entity processes damage when hurt.
     * This event won't be fired when {@link LivingEntityEvent#HURT} event is cancelled.
     * <p>
     * Runs on both Client and Server logical side and can be cancelled.
     */
    Event1<HurtProcessed> HURT_PROCESSED = new Event1<>();

    abstract class AbstractLivingEntityContext extends AbstractWorldEventContext
    {
        protected AbstractLivingEntityContext(LivingEntity entity)
        {
            super(entity.level());
        }
    }

    final class DiedContext extends AbstractLivingEntityContext
    {
        @Getter
        private final DamageSource damageSource;

        @ApiStatus.Internal
        public DiedContext(LivingEntity entity, DamageSource damageSource)
        {
            super(entity);
            this.damageSource = damageSource;
        }
    }

    final class HurtContext extends AbstractLivingEntityContext implements ICancellableEventContext
    {
        @Getter
        private final float originalDamage;

        @Getter @Setter
        private float damage;

        @Getter
        private final DamageSource damageSource;

        @Getter @Setter
        private boolean isCancelled;

        public boolean isDamageModified()
        {
            return damage != originalDamage;
        }

        public void addDamage(float damage)
        {
            this.damage += damage;
        }

        public void subDamage(float damage)
        {
            this.damage -= damage;
        }

        public void mulDamage(float damage)
        {
            this.damage *= damage;
        }

        public void divDamage(float damage)
        {
            this.damage /= damage;
        }

        @ApiStatus.Internal
        public HurtContext(LivingEntity entity, DamageSource damageSource, float damage)
        {
            super(entity);
            this.originalDamage = damage;
            this.damage = damage;
            this.damageSource = damageSource;
        }
    }

    final class HurtProcessed extends AbstractLivingEntityContext
    {
        @Getter
        private final float damage;

        @Getter
        private final DamageSource damageSource;

        @ApiStatus.Internal
        public HurtProcessed(LivingEntity entity, DamageSource damageSource, float damage)
        {
            super(entity);
            this.damage = damage;
            this.damageSource = damageSource;
        }
    }
}
