package io.pastework.test.common.event;

import io.pastework.core.api.common.event.Event1;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public interface ServerPlayerSpellEvent
{
    Event1<SelectRequestContext> SELECT_REQUEST = new Event1<>();

    Event1<CastRequestContext> CAST_REQUEST = new Event1<>();

    abstract class AbstractContext
    {
        @Getter
        private final ServerPlayer player;

        public void disconnectPlayer(String reason)
        {
            player.connection.disconnect(Component.literal(reason));
        }

        protected AbstractContext(ServerPlayer player)
        {
            this.player = player;
        }
    }

    final class SelectRequestContext extends AbstractContext
    {
        @Getter
        private final int spellIndex;

        public SelectRequestContext(ServerPlayer player, int spellIndex)
        {
            super(player);
            this.spellIndex = spellIndex;
        }
    }

    final class CastRequestContext extends AbstractContext
    {
        public CastRequestContext(ServerPlayer player)
        {
            super(player);
        }
    }
}
