package io.pastework.core.base.client.impl.service.ui;

import io.pastework.core.api.client.service.ui.IGuiLayerRenderable;
import lombok.Getter;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

@Getter
public final class GuiRegistryEntry
{
    private final GuiRegisterLayerOrder layerOrder;
    @Nullable private final Identifier targetId;
    @Nullable private final Identifier id;
    private final IGuiLayerRenderable gui;

    public static GuiRegistryEntry replace(Identifier targetId, IGuiLayerRenderable hud)
    {
        return new GuiRegistryEntry(
            GuiRegisterLayerOrder.REPLACE,
            targetId,
            null,
            hud
        );
    }

    public static GuiRegistryEntry aboveAll(Identifier id, IGuiLayerRenderable hud)
    {
        return new GuiRegistryEntry(
            GuiRegisterLayerOrder.ABOVE_ALL,
            null,
            id,
            hud
        );
    }

    public static GuiRegistryEntry belowAll(Identifier id, IGuiLayerRenderable hud)
    {
        return new GuiRegistryEntry(
            GuiRegisterLayerOrder.BELOW_ALL,
            null,
            id,
            hud
        );
    }

    public static GuiRegistryEntry after(
        Identifier targetId,
        Identifier id,
        IGuiLayerRenderable hud
    )
    {
        return new GuiRegistryEntry(
            GuiRegisterLayerOrder.ABOVE,
            targetId,
            id,
            hud
        );
    }

    public static GuiRegistryEntry below(
        Identifier targetId,
        Identifier id,
        IGuiLayerRenderable hud
    )
    {
        return new GuiRegistryEntry(
            GuiRegisterLayerOrder.BELOW,
            targetId,
            id,
            hud
        );
    }

    private GuiRegistryEntry(
        GuiRegisterLayerOrder layerOrder,
        @Nullable Identifier targetId,
        @Nullable Identifier id,
        IGuiLayerRenderable gui
    )
    {
        this.layerOrder = layerOrder;
        this.targetId = targetId;
        this.id = id;
        this.gui = gui;
    }
}
