package io.pastework.test.client.event;

import io.pastework.core.api.common.event.Event1;
import io.pastework.test.common.spell.SpellSyncRecord;
import lombok.Getter;
import net.minecraft.client.player.LocalPlayer;

import java.util.Collection;

public interface ClientSpellEvent
{
    Event1<SpellSyncContext> SPELL_SYNC = new Event1<>();

    abstract class AbstractContext
    {
        @Getter
        private final LocalPlayer localPlayer;

        protected AbstractContext(LocalPlayer localPlayer)
        {
            this.localPlayer = localPlayer;
        }
    }

    final class SpellSyncContext extends AbstractContext
    {
        @Getter
        private final int selectedSpellIndex;

        @Getter
        private final boolean needNewCache;

        @Getter
        private final Collection<SpellSyncRecord> spellSyncList;

        public SpellSyncContext(
            LocalPlayer localPlayer,
            int selectedSpellIndex,
            boolean needNewCache,
            Collection<SpellSyncRecord> spellSyncList
        )
        {
            super(localPlayer);
            this.selectedSpellIndex = selectedSpellIndex;
            this.needNewCache = needNewCache;
            this.spellSyncList = spellSyncList;
        }
    }
}
