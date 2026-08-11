package io.pastework.core.fabric.common.impl.internal;

import io.pastework.core.base.common.hook.LevelHooks;
import io.pastework.core.base.common.hook.ServerHooks;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class FabricCommonEventRegistrator
{
    private static boolean initialized;

    public static void initialize()
    {
        if (initialized)
        {
            throw new IllegalStateException("FabricCommonEventRegistrator already initialized");
        }

        ServerLifecycleEvents.SERVER_STARTING.register(ServerHooks::fireServerStartingEvent);
        ServerLifecycleEvents.SERVER_STARTED.register(ServerHooks::fireServerStartedEvent);
        ServerLifecycleEvents.SERVER_STOPPING.register(ServerHooks::fireServerStoppingEvent);
        ServerLifecycleEvents.SERVER_STOPPED.register(ServerHooks::fireServerStoppedEvent);

        ServerTickEvents.START_WORLD_TICK.register(LevelHooks::fireLevelPreTickEvent);
        ServerTickEvents.END_WORLD_TICK.register(LevelHooks::fireLevelPostTickEvent);

        UseBlockCallback.EVENT.register((
            player,
            world,
            hand,
            hitResult
        ) ->
        {
            var event = LevelHooks.firePlayerBlockInteractEvent(
                player,
                player.getItemInHand(hand),
                hand,
                hitResult.getBlockPos()
            );

            return event.getEditedInteractionResult().orElse(InteractionResult.PASS);
        });

        UseItemCallback.EVENT.register((player, world, hand) ->
        {
            var context = LevelHooks.firePlayerItemUseEvent(player, player.getItemInHand(hand), hand);

            if (context.isCancelled())
            {
                return InteractionResult.FAIL;
            }

            return context.getEditedInteractionResult().orElse(InteractionResult.PASS);
        });

        UseEntityCallback.EVENT.register((
            Player player,
            Level world,
            InteractionHand hand,
            Entity entity,
            @Nullable EntityHitResult hitResult
        ) ->
        {
            var context = LevelHooks.firePlayerEntityInteractEvent(player, entity, hand);

            if (context.isCancelled())
            {
                return InteractionResult.FAIL;
            }

            return context.getEditedInteractionResult()
                .orElse(InteractionResult.PASS);
        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((
            player,
            origin,
            destination
        ) -> LevelHooks.firePlayerChangedDimensionEvent(
            player,
            origin.dimension(), destination.dimension()
        ));

        ServerPlayConnectionEvents.JOIN.register((
            handler,
            sender,
            server
        ) -> LevelHooks.firePlayerJoinEvent(
            handler.getPlayer()
        ));

        ServerPlayConnectionEvents.DISCONNECT.register((
            handler,
            server
        ) -> LevelHooks.firePlayerLeaveEvent(handler.getPlayer()));

        ServerPlayerEvents.COPY_FROM.register((
            oldPlayer,
            newPlayer,
            alive
        ) -> LevelHooks.firePlayerCloneEvent(newPlayer, oldPlayer, !alive));

        ServerPlayerEvents.AFTER_RESPAWN.register((
            oldPlayer,
            newPlayer,
            alive
        ) -> LevelHooks.firePlayerRespawnEvent(newPlayer, alive));

        initialized = true;
    }
}
