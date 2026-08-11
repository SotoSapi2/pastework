package io.pastework.core.fabric.common.impl.service.network;

import io.pastework.core.api.common.service.network.NetworkThreadType;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.mixin.networking.accessor.ServerCommonPacketListenerImplAccessor;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public final class FabricServerPacketContext extends AbstractFabricPacketContext
 {
     public static FabricServerPacketContext fromPlayContext(
         NetworkThreadType threadType,
         ServerPlayNetworking.Context context
     )
     {
         var accessor = (ServerCommonPacketListenerImplAccessor) context.player().connection;

         return new FabricServerPacketContext(
             PacketFlow.CLIENTBOUND,
             threadType,
             accessor.getConnection(),
             context.player().connection,
             context.player(),
             context.responseSender()
         );
     }

     public static FabricServerPacketContext fromConfigContext(
         NetworkThreadType threadType,
         ServerConfigurationNetworking.Context context
     )
     {
         var accessor = (ServerCommonPacketListenerImplAccessor) context.networkHandler();
         return new FabricServerPacketContext(
             PacketFlow.CLIENTBOUND,
             threadType,
             accessor.getConnection(),
             context.networkHandler(),
             null,
             context.responseSender()
         );
     }

     private FabricServerPacketContext(
         PacketFlow packetFlow,
         NetworkThreadType threadType,
         Connection connection,
         PacketListener packetListener,
         @Nullable Player player,
         PacketSender packetSender
     )
     {
         super(packetFlow, threadType, connection, packetListener, player, packetSender);
     }

     @Override
     public boolean canAccept(CustomPacketPayload.@NonNull Type<?> type)
     {
        if(getListener() instanceof ServerPlayer listener)
        {
            return ServerPlayNetworking.canSend(listener, type);
        }

        if(getListener() instanceof ServerConfigurationPacketListenerImpl listener)
        {
            return ServerConfigurationNetworking.canSend(listener, type);
        }

         return false;
     }
 }
