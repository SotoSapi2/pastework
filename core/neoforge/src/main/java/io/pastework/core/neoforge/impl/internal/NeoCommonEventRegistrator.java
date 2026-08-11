package io.pastework.core.neoforge.impl.internal;

import io.pastework.core.api.common.event.world.PlayerInteractionEvent;
import io.pastework.core.base.common.hook.LevelHooks;
import io.pastework.core.base.common.hook.ServerHooks;
import lombok.experimental.UtilityClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.TriState;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

@UtilityClass
@ApiStatus.Internal
public final class NeoCommonEventRegistrator
{
    private static boolean initialized;

    public static void initialize()
    {
        if (initialized)
        {
            throw new IllegalStateException();
        }

        NeoForge.EVENT_BUS.register(NeoCommonEventRegistrator.class);
        initialized = true;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(LivingIncomingDamageEvent event)
    {
        var pasteEvent = LevelHooks.firePreLivingEntityHurtEvent(
            event.getEntity(),
            event.getSource(),
            event.getAmount()
        );

        if (pasteEvent.isCancelled())
        {
            event.setCanceled(true);
        }
        else if (pasteEvent.isDamageModified())
        {
            event.setAmount(pasteEvent.getDamage());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(LivingDamageEvent.Post event)
    {
        LevelHooks.firePostLivingEntityHurtEvent(
            event.getEntity(),
            event.getSource(),
            event.getOriginalDamage()
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(LivingDeathEvent event)
    {
        LevelHooks.fireLivingEntityDiedEvent(event.getEntity(), event.getSource());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(ServerChatEvent event)
    {
        var pasteEvent = ServerHooks.fireChatReceivedEvent(event.getPlayer(), event.getMessage());

        if (pasteEvent.isCancelled())
        {
            event.setCanceled(true);
        }
        else if (pasteEvent.isModified())
        {
            event.setMessage(pasteEvent.getMessage());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(LevelTickEvent.Pre event)
    {
        LevelHooks.fireLevelPreTickEvent(event.getLevel());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(LevelTickEvent.Post event)
    {
        LevelHooks.fireLevelPostTickEvent(event.getLevel());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(PlayerTickEvent.Pre event)
    {
        LevelHooks.firePlayerPreTickEvent(event.getEntity());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(PlayerTickEvent.Post event)
    {
        LevelHooks.firePlayerPostTickEvent(event.getEntity());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(PlayerInteractEvent.RightClickItem event)
    {
        var context = LevelHooks.firePlayerItemUseEvent(
            event.getEntity(),
            event.getItemStack(),
            event.getHand()
        );

        if (context.isCancelled())
        {
            event.setCanceled(true);
        }

        context.getEditedInteractionResult()
            .ifPresent(event::setCancellationResult);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(PlayerInteractEvent.LeftClickBlock event)
    {
        if (event.getAction() == PlayerInteractEvent.LeftClickBlock.Action.CLIENT_HOLD)
        {
            return;
        }

        PlayerInteractionEvent.BlockAction action = switch (event.getAction())
        {
            case START -> PlayerInteractionEvent.BlockAction.START;
            case STOP -> PlayerInteractionEvent.BlockAction.STOP;
            case ABORT -> PlayerInteractionEvent.BlockAction.ABORT;
            default -> throw new AssertionError(
                "Pastework BlockInteractionEvent.BlockAttack shouldn't handle " + event.getAction()
            );
        };

        var attackContext = LevelHooks.firePlayerBlockAttackEvent(
            event.getEntity(),
            event.getItemStack(),
            event.getHand(),
            action,
            event.getPos()
        );

        if (attackContext.isCancelled())
        {
            event.setCanceled(true);
        }
        else
        {
            var damageContext = LevelHooks.firePlayerBlockDamageEvent(
                event.getEntity(),
                event.getItemStack(),
                event.getHand(),
                action,
                event.getPos()
            );

            if (damageContext.isCancelled())
            {
                event.setUseBlock(TriState.TRUE);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(PlayerInteractEvent.RightClickBlock event)
    {
        var context = LevelHooks.firePlayerBlockInteractEvent(
            event.getEntity(),
            event.getItemStack(),
            event.getHand(),
            event.getPos()
        );

        if (context.isInteractionResultEdited())
        {
            Optional<InteractionResult> result = context.getEditedInteractionResult();
            assert result.isPresent();

            event.setCanceled(true);
            event.setCancellationResult(result.get());
        }
        else if (context.isCancelled())
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(PlayerInteractEvent.EntityInteract event)
    {
        var context = LevelHooks.firePlayerEntityInteractEvent(
            event.getEntity(),
            event.getTarget(),
            event.getHand()
        );

        if (context.isCancelled())
        {
            event.setCanceled(true);
        }

        context.getEditedInteractionResult()
            .ifPresent(event::setCancellationResult);
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        LevelHooks.firePlayerChangedDimensionEvent(
            (ServerPlayer) event.getEntity(),
            event.getFrom(),
            event.getTo()
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(PlayerEvent.PlayerRespawnEvent event)
    {
        LevelHooks.firePlayerRespawnEvent(
            (ServerPlayer) event.getEntity(),
            event.isEndConquered()
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(ServerStartingEvent event)
    {
        ServerHooks.fireServerStartingEvent(event.getServer());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(PlayerEvent.PlayerLoggedInEvent event)
    {
        LevelHooks.firePlayerJoinEvent((ServerPlayer) event.getEntity());
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(PlayerEvent.PlayerLoggedOutEvent event)
    {
        LevelHooks.firePlayerLeaveEvent((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(ServerStartedEvent event)
    {
        ServerHooks.fireServerStartedEvent(event.getServer());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(ServerStoppingEvent event)
    {
        ServerHooks.fireServerStoppingEvent(event.getServer());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(ServerStoppedEvent event)
    {
        ServerHooks.fireServerStoppedEvent(event.getServer());
    }
}
