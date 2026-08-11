package io.pastework.core.fabric.common.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import io.pastework.core.base.common.hook.LevelHooks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class FabricLivingEntityMixin
{
    @Inject(
        method = "hurtServer",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/LivingEntity;noActionTime:I",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void hurt(
        ServerLevel level,
        DamageSource damageSource,
        float amount,
        CallbackInfoReturnable<Boolean> cir,
        @Local(argsOnly = true) LocalFloatRef amountRef
    )
    {
        var entity = (LivingEntity) (Object) this;
        var event = LevelHooks.firePreLivingEntityHurtEvent(entity, damageSource, amount);

        if(event.isDamageModified())
        {
            amountRef.set(event.getDamage());
        }

        if(event.isCancelled())
        {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "actuallyHurt", at = @At("RETURN"))
    private void hurtPost(
        ServerLevel level,
        DamageSource damageSource,
        float amount,
        CallbackInfo ci
    )
    {
        var entity = (LivingEntity) (Object) this;

        if(!entity.isInvulnerableTo(level, damageSource))
        {
            LevelHooks.firePostLivingEntityHurtEvent(entity, damageSource, amount);
        }
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void die(DamageSource damageSource, CallbackInfo ci)
    {
        var entity = (LivingEntity) (Object) this;
        LevelHooks.fireLivingEntityDiedEvent(entity, damageSource);
    }
}