package io.pastework.core.base.client.impl.service.ui;

import io.pastework.core.api.client.service.ui.IGuiContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class DefaultGuiContext implements IGuiContext
{
    private final Minecraft minecraft;
    private final GuiGraphics graphics;

    public DefaultGuiContext(Minecraft minecraft, GuiGraphics graphics)
    {
        this.minecraft = minecraft;
        this.graphics = graphics;
    }

    @Override
    public Minecraft getMinecraft()
    {
        return minecraft;
    }

    @Override
    public GuiGraphics getGraphics()
    {
        return graphics;
    }
}
