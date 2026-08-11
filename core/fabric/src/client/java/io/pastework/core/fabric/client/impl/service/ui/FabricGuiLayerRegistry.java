package io.pastework.core.fabric.client.impl.service.ui;

import io.pastework.core.base.client.impl.service.ui.AbstractGuiLayerRegistry;
import io.pastework.core.base.client.impl.service.ui.DefaultGuiContext;
import io.pastework.core.base.client.impl.service.ui.GuiRegistryEntry;
import io.pastework.core.fabric.common.impl.service.IFabricRegistrable;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;

public class FabricGuiLayerRegistry extends AbstractGuiLayerRegistry implements IFabricRegistrable
{
    @Override
    public boolean isRegistrationFinalized()
    {
        return super.isRegistrationFinalized();
    }

    @Override
    public void processRegistration()
    {
        if (isRegistrationFinalized())
        {
            throw new IllegalStateException();
        }

        for (GuiRegistryEntry entry : getHudRegistrySets())
        {
            HudElement adapter = (graphics, tickDelta) ->
            {
                var ctx = new DefaultGuiContext(Minecraft.getInstance(), graphics);
                entry.getGui().renderGuiLayer(ctx);
            };

            switch (entry.getLayerOrder())
            {
                case ABOVE ->
                {
                    assert entry.getId() != null;
                    assert entry.getTargetId() != null;

                    HudElementRegistry.attachElementAfter(
                        entry.getTargetId(),
                        entry.getId(),
                        adapter
                    );
                }
                case BELOW ->
                {
                    assert entry.getId() != null;
                    assert entry.getTargetId() != null;

                    HudElementRegistry.attachElementBefore(
                        entry.getTargetId(),
                        entry.getId(),
                        adapter
                    );
                }
                case REPLACE ->
                {
                    assert entry.getTargetId() != null;
                    assert entry.getId() == null;

                    HudElementRegistry.replaceElement(
                        entry.getTargetId(),
                        old -> adapter
                    );
                }
                case ABOVE_ALL ->
                {
                    assert entry.getId() != null;
                    assert entry.getTargetId() == null;

                    HudElementRegistry.addLast(
                        entry.getId(),
                        adapter
                    );
                }
                case BELOW_ALL ->
                {
                    assert entry.getId() != null;
                    assert entry.getTargetId() == null;

                    HudElementRegistry.addFirst(
                        entry.getId(),
                        adapter
                    );
                }
            }
        }

        finalizeRegistration();
    }
}
