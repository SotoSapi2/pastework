package io.pastework.core.neoforge.impl.internal;

import io.pastework.core.base.client.hook.ClientHooks;
import io.pastework.core.base.common.hook.BootstrapHooks;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.ClientChatEvent;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.ApiStatus;

@UtilityClass
@ApiStatus.Internal
public final class NeoClientEventRegistrator
{
    private static boolean initialized;

    public static void initialize()
    {
        if (initialized)
        {
            throw new IllegalStateException();
        }

        NeoForge.EVENT_BUS.register(NeoClientEventRegistrator.class);
        initialized = true;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(ClientTickEvent.Pre event)
    {
        ClientHooks.fireClientPreTickEvent(Minecraft.getInstance());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(ClientTickEvent.Post event)
    {
        ClientHooks.fireClientPostTickEvent(Minecraft.getInstance());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(ClientChatReceivedEvent event)
    {
        var pasteEvent = ClientHooks.fireChatReceivedEvent(event.getMessage());

        if (pasteEvent.isCancelled())
        {
            event.setCanceled(true);
        }
        else if (pasteEvent.isMessageModified())
        {
            event.setMessage(pasteEvent.getMessage());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(ClientChatEvent event)
    {
        var pasteEvent = ClientHooks.fireChatSendEvent(event.getMessage());

        if (pasteEvent.isCancelled())
        {
            event.setCanceled(true);
        }
        else if (pasteEvent.isMessageModified())
        {
            event.setMessage(pasteEvent.getMessage());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(RenderLevelStageEvent.AfterSky event)
    {
        ClientHooks.fireAfterSkyEvent(
            event.getLevelRenderState(),
            event.getModelViewMatrix()
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(RenderLevelStageEvent.AfterLevel event)
    {
        ClientHooks.fireAfterLevelEvent(
            event.getLevelRenderState(),
            event.getModelViewMatrix()
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(RenderLevelStageEvent.AfterEntities event)
    {
        ClientHooks.fireAfterEntitiesEvent(
            event.getLevelRenderState(),
            event.getModelViewMatrix(),
            event.getPoseStack()
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(RenderLevelStageEvent.AfterParticles event)
    {
        ClientHooks.fireAfterParticlesEvent(
            event.getLevelRenderState(),
            event.getModelViewMatrix()
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(RenderLevelStageEvent.AfterOpaqueBlocks event)
    {
        ClientHooks.fireAfterOpaqueBlocksEvent(
            event.getLevelRenderState(),
            event.getModelViewMatrix()
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(RenderLevelStageEvent.AfterTranslucentBlocks event)
    {
        ClientHooks.fireAfterTranslucentBlocksEvent(
            event.getLevelRenderState(),
            event.getModelViewMatrix(),
            event.getPoseStack()
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(RenderLevelStageEvent.AfterTripwireBlocks event)
    {
        ClientHooks.fireAfterTripwireBlocksEvent(
            event.getLevelRenderState(),
            event.getModelViewMatrix(),
            event.getPoseStack()
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    private static void event(RenderLevelStageEvent.AfterWeather event)
    {
        ClientHooks.fireAfterWeatherEvent(
            event.getLevelRenderState(),
            event.getModelViewMatrix()
        );
    }
}
