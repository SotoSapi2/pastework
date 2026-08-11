package io.pastework.core.api.client.event;

import io.pastework.core.api.common.event.Event1;
import net.minecraft.client.Minecraft;

/**
 * Fired every client tick.
 * <p>
 * <strong>Running Side:</strong> Client.
 */
public interface ClientTickEvent {
    Event1<Minecraft> PRE = new Event1<>();

    Event1<Minecraft> POST = new Event1<>();
}
