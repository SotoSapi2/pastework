package io.pastework.core.neoforge.impl.service.client.ui;

import io.pastework.core.api.client.service.ui.IGuiLayerRenderable;
import io.pastework.core.base.client.impl.service.ui.AbstractGuiLayerRegistry;
import io.pastework.core.base.client.impl.service.ui.DefaultGuiContext;
import io.pastework.core.base.client.impl.service.ui.GuiRegistryEntry;
import io.pastework.core.neoforge.impl.service.INeoEventBusDependant;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.jetbrains.annotations.NotNull;

public final class NeoGuiLayerRegistry extends AbstractGuiLayerRegistry implements INeoEventBusDependant
{
    @SubscribeEvent
    public void onHudRegister(RegisterGuiLayersEvent event)
    {
        for (GuiRegistryEntry entry : getHudRegistrySets())
        {
            var adapter = new HudAdapter(entry.getGui());

            switch (entry.getLayerOrder())
            {
                case ABOVE ->
                {
                    assert entry.getId() != null;
                    assert entry.getTargetId() != null;

                    event.registerAbove(
                        entry.getTargetId(),
                        entry.getId(),
                        adapter
                    );
                }
                case BELOW ->
                {
                    assert entry.getId() != null;
                    assert entry.getTargetId() != null;

                    event.registerBelow(
                        entry.getTargetId(),
                        entry.getId(),
                        adapter
                    );
                }
                case REPLACE ->
                {
                    assert entry.getTargetId() != null;
                    assert entry.getId() == null;

                    event.replaceLayer(
                        entry.getTargetId(),
                        adapter
                    );
                }
                case ABOVE_ALL ->
                {
                    assert entry.getId() != null;
                    assert entry.getTargetId() == null;

                    event.registerAboveAll(
                        entry.getId(),
                        adapter
                    );
                }
                case BELOW_ALL ->
                {
                    assert entry.getId() != null;
                    assert entry.getTargetId() == null;

                    event.registerBelowAll(
                        entry.getId(),
                        adapter
                    );
                }
            }
        }

        finalizeRegistration();
    }

    @Override
    public void handleEventBus(IEventBus eventBus)
    {
        eventBus.register(this);
    }

    private record HudAdapter(IGuiLayerRenderable hud) implements GuiLayer
    {
        @Override
        public void render(
            @NotNull GuiGraphics drawer,
            @NotNull DeltaTracker deltaTracker
        )
        {
            var ctx = new DefaultGuiContext(
              Minecraft.getInstance(),
              drawer
            );

            hud.renderGuiLayer(ctx);
        }
    }
}
