package io.pastework.core.api.common.service.network;

import io.pastework.spi.IPasteworkService;
import net.minecraft.network.RegistryFriendlyByteBuf;

/**
 * A network channel for registering and managing gameplay related packets.
 */
public interface IPlayNetworkChannel extends INetworkChannel<RegistryFriendlyByteBuf>
{ }
