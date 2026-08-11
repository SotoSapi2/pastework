package io.pastework.core.api.client.service.ui;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public interface IGuiContext
{
    Minecraft getMinecraft();

    GuiGraphics getGraphics();

    default Window getWindow()
    {
        return getMinecraft()
            .getWindow();
    }

    default MouseHandler getMouseHandler()
    {
        return getMinecraft().mouseHandler;
    }

    default DeltaTracker getDeltaTracker()
    {
        return getMinecraft().getDeltaTracker();
    }

    default Font getDefaultFont()
    {
        return getMinecraft().font;
    }

    default int getGuiScaledWidth()
    {
        return getWindow().getGuiScaledWidth();
    }

    default int getGuiScaledHeight()
    {
        return getWindow().getGuiScaledHeight();
    }

    default int getScreenWidth()
    {
        return getWindow().getScreenWidth();
    }

    default int getScreenHeight()
    {
        return getWindow().getScreenHeight();
    }
}
