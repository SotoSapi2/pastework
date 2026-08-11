package io.pastework.core.fabric.common.mixin.world;

import io.pastework.core.api.common.event.world.PlayerInteractionEvent;
import io.pastework.core.base.common.hook.LevelHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerGameMode.class)
public class FabricServerPlayerGameModeMixin
{
    @Shadow
    @Final
    protected ServerPlayer player;

    @Inject(method = "handleBlockBreakAction", at = @At("HEAD"), cancellable = true)
    private void handleBlockBreakAction_head(
        BlockPos pos,
        ServerboundPlayerActionPacket.Action action,
        Direction face,
        int maxBuildHeight,
        int sequence,
        CallbackInfo ci
    )
    {
        PlayerInteractionEvent.BlockAction eventAction = PlayerInteractionEvent.BlockAction
            .fromPacketAction(action);

        var event = LevelHooks.firePlayerBlockDamageEvent(
            player,
            player.getItemInHand(InteractionHand.MAIN_HAND),
            InteractionHand.MAIN_HAND,
            eventAction,
            pos
        );

        if (event.isCancelled())
        {
            ci.cancel();
        }
    }
}
