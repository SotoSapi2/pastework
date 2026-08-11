package io.pastework.core.fabric.client.mixin.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.pastework.core.api.common.event.world.PlayerInteractionEvent;
import io.pastework.core.base.common.hook.LevelHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class FabricMultiPlayerGameModeMixin
{
    @Inject(method = "method_41936", at = @At("HEAD"), cancellable = true)
    public void startBlockBreak_startPredict_lambda0(
        BlockPos blockPos,
        Direction direction,
        int i,
        CallbackInfoReturnable<ServerboundPlayerActionPacket> cir
    )
    {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        assert player != null;

        var event = LevelHooks.firePlayerBlockDamageEvent(
            player,
            player.getMainHandItem(),
            InteractionHand.MAIN_HAND,
            PlayerInteractionEvent.BlockAction.START,
            blockPos
        );

        if (event.isCancelled())
        {
            var returnPacket = new ServerboundPlayerActionPacket(
                ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK,
                blockPos,
                direction,
                i
            );

            cir.setReturnValue(returnPacket);
        }
    }

    // i hope it doesn't really matter if inject event caller after Tutorial::onDestroyBlock duh
    @WrapOperation(
        method = "method_41930",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z")
    )
    public boolean startBlockBreak_startPredict_lambda1_wrap$isAir0(
        BlockState instance,
        Operation<Boolean> original,
        @Local(type = BlockPos.class, argsOnly = true) BlockPos pos
    )
    {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        assert player != null;

        var event = LevelHooks.firePlayerBlockDamageEvent(
            player,
            player.getMainHandItem(),
            InteractionHand.MAIN_HAND,
            PlayerInteractionEvent.BlockAction.START,
            pos
        );

        if (event.isCancelled())
        {
            return false;
        }

        return original.call(instance);
    }

    @Inject(method = "method_41935", at = @At("HEAD"), cancellable = true)
    public void continueBlockBreak_startPredict_lambda0(
        BlockPos blockPos,
        Direction direction,
        int i,
        CallbackInfoReturnable<ServerboundPlayerActionPacket> cir
    )
    {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        assert player != null;

        var event = LevelHooks.firePlayerBlockDamageEvent(
            player,
            player.getMainHandItem(),
            InteractionHand.MAIN_HAND,
            PlayerInteractionEvent.BlockAction.START,
            blockPos
        );

        if (event.isCancelled())
        {
            var returnPacket = new ServerboundPlayerActionPacket(
                ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK,
                blockPos,
                direction,
                i
            );

            cir.setReturnValue(returnPacket);
        }
    }
}
