package io.pastework.core.base.common.hook;

import io.pastework.core.api.common.event.world.*;
import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;

@UtilityClass
@ApiStatus.Internal
public final class LevelHooks
{
    public static void fireLevelPreTickEvent(Level level)
    {
        LevelTickEvent.PRE.fire(level);
    }

    public static void fireLevelPostTickEvent(Level level)
    {
        LevelTickEvent.POST.fire(level);
    }

    public static PlayerInteractionEvent.ItemInteractContext firePlayerItemUseEvent(
        Player player,
        ItemStack stack,
        InteractionHand hand
    )
    {
        var ctx = new PlayerInteractionEvent.ItemInteractContext(player, stack, hand);

        PlayerInteractionEvent.ITEM_INTERACT.fire(ctx);
        return ctx;
    }

    public static PlayerInteractionEvent.BlockInteractContext firePlayerBlockInteractEvent(
        Player player,
        ItemStack stack,
        InteractionHand hand,
        BlockPos blockPos
    )
    {
        var ctx = new PlayerInteractionEvent.BlockInteractContext(
            player,
            stack,
            hand,
            blockPos
        );

        PlayerInteractionEvent.BLOCK_INTERACT.fire(ctx);
        return ctx;
    }

    public static void firePlayerPreTickEvent(Player player)
    {
        var ctx = new PlayerEntityEvent.TickContext(player);
        PlayerEntityEvent.PRE_TICK.fire(ctx);
    }

    public static void firePlayerPostTickEvent(Player player)
    {
        var ctx = new PlayerEntityEvent.TickContext(player);
        PlayerEntityEvent.POST_TICK.fire(ctx);
    }
    
    public static LivingEntityEvent.HurtContext firePreLivingEntityHurtEvent(
        LivingEntity entity,
        DamageSource source,
        float amount
    )
    {
        var ctx = new LivingEntityEvent.HurtContext(entity, source, amount);
        LivingEntityEvent.HURT.fire(ctx);
        return ctx;
    }

    public static void firePostLivingEntityHurtEvent(LivingEntity entity, DamageSource source, float amount)
    {
        var ctx = new LivingEntityEvent.HurtProcessed(entity, source, amount);
        LivingEntityEvent.HURT_PROCESSED.fire(ctx);
    }

    public static void fireLivingEntityDiedEvent(LivingEntity entity, DamageSource source)
    {
        if(entity instanceof Player player)
        {
            var ctx = new PlayerEntityEvent.DiedContext(player, source);
            PlayerEntityEvent.DIED.fire(ctx);
        }

        var ctx = new LivingEntityEvent.DiedContext(entity, source);
        LivingEntityEvent.DIED.fire(ctx);
    }
    
    public static void firePlayerJoinEvent(ServerPlayer player)
    {
        ServerPlayerEvent.JOIN.fire(player);
    }

    public static void firePlayerLeaveEvent(ServerPlayer player)
    {
        ServerPlayerEvent.LEAVE.fire(player);
    }

    public static void firePlayerCloneEvent(ServerPlayer player, ServerPlayer original, boolean wasDeath)
    {
        var ctx = new ServerPlayerEvent.CloneContext(original, wasDeath);
        ServerPlayerEvent.CLONE.fire(ctx, player);
    }

    public static void firePlayerChangedDimensionEvent(
        ServerPlayer player,
        ResourceKey<Level> from,
        ResourceKey<Level> to
    )
    {
        var ctx = new ServerPlayerEvent.ChangedDimensionContext(from, to);
        ServerPlayerEvent.CHANGED_LEVEL.fire(ctx, player);
    }

    public static void firePlayerRespawnEvent(ServerPlayer player, boolean endConquered)
    {
        var ctx = new ServerPlayerEvent.RespawnContext(endConquered);
        ServerPlayerEvent.RESPAWN.fire(ctx, player);
    }

    public static PlayerInteractionEvent.BlockAttackContext firePlayerBlockAttackEvent(
        Player player,
        ItemStack stack,
        InteractionHand hand,
        PlayerInteractionEvent.BlockAction action,
        BlockPos blockPos
    )
    {
        var ctx = new PlayerInteractionEvent.BlockAttackContext(player, stack, hand, action, blockPos);
        PlayerInteractionEvent.BLOCK_ATTACK.fire(ctx);
        
        return ctx;
    }

    public static PlayerInteractionEvent.BlockDamageContext firePlayerBlockDamageEvent(
        Player player,
        ItemStack stack,
        InteractionHand hand,
        PlayerInteractionEvent.BlockAction action,
        BlockPos blockPos
    )
    {
        var ctx = new PlayerInteractionEvent.BlockDamageContext(player, stack, hand, action, blockPos);
        PlayerInteractionEvent.BLOCK_DAMAGE.fire(ctx);
        
        return ctx;
    }

    public static PlayerInteractionEvent.EntityInteractContext firePlayerEntityInteractEvent(
        Player player,
        Entity entity,
        InteractionHand hand
    )
    {
        var ctx = new PlayerInteractionEvent.EntityInteractContext(player, entity, hand);
        PlayerInteractionEvent.ENTITY_INTERACT.fire(ctx);
        
        return ctx;
    }
}
