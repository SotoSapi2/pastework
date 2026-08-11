package io.pastework.core.api.client.event.rendering;

import io.pastework.core.api.common.event.Event2;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;

public interface FrameRenderEvent
{
    Event2<Minecraft, DeltaTracker> PRE = new Event2<>();

    Event2<Minecraft, DeltaTracker> POST = new Event2<>();
}
