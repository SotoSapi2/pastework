package io.pastework.test.client.controller;

import io.pastework.core.api.client.service.network.IClientNetworking;
import io.pastework.core.api.client.service.network.IPlayServerRemote;
import io.pastework.core.api.common.event.IEventConnection;
import io.pastework.core.api.common.event.lifecycle.BootstrapEvent;
import io.pastework.core.api.common.event.world.PlayerEntityEvent;
import io.pastework.test.client.event.ClientSpellEvent;
import io.pastework.test.common.network.clientbound.play.spell.ClientboundSpellSync;
import io.pastework.test.common.network.serverbound.play.spell.ServerboundCastSpell;
import io.pastework.test.common.network.serverbound.play.spell.ServerboundSelectSpell;
import io.pastework.test.common.network.serverbound.play.spell.ServerboundSyncSpellRequest;
import io.pastework.test.common.registry.Spells;
import io.pastework.test.common.spell.AbstractSpell;
import io.pastework.test.common.spell.SpellSyncRecord;
import io.pastework.test.common.spell.SpellType;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.util.ProblemReporter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class ClientSorceryController
{
    public static final int INVALID_SPELL_INDEX = -1;
    private final IClientNetworking clientNetworking = IClientNetworking.getService();
    private final IPlayServerRemote serverRemote = IPlayServerRemote.getRemote();
    private final List<AbstractSpell> spellList = new ArrayList<>();
    private final List<IEventConnection> eventConnectionList = new ArrayList<>();

    @Getter
    private @Nullable AbstractSpell selectedSpell;

    @Getter
    private int selectedSpellIndex = 0;

    public void initialize()
    {
        eventConnectionList.add(
            PlayerEntityEvent.PRE_TICK.connect(this::onTick)
        );

        eventConnectionList.add(
            ClientSpellEvent.SPELL_SYNC.connect(this::onSpellSync)
        );

        eventConnectionList.add(
            BootstrapEvent.COMMON_SETUP.connect(this::initNetworkEvent)
        );
    }

    public void cleanUpCache()
    {
        selectedSpell = null;
        selectedSpellIndex = 0;
        spellList.clear();
    }

    public void close()
    {
        cleanUpCache();

        for(var connection : eventConnectionList)
        {
            connection.disconnect();
        }
    }

    public List<AbstractSpell> getSpellList()
    {
        return Collections.unmodifiableList(spellList);
    }

    public void requestSync()
    {
        serverRemote.send(ServerboundSyncSpellRequest.INSTANCE);
    }

    public boolean trySelectSpell(int index)
    {
        if(index <= INVALID_SPELL_INDEX)
        {
            return false;
        }

        if (index >= spellList.size())
        {
            return false;
        }

        selectedSpellIndex = index;
        var spell = spellList.get(index);

        if (selectedSpell != spell)
        {
            selectedSpell = spellList.get(index);
        }

        serverRemote.send(new ServerboundSelectSpell(index));

        return true;
    }

    public boolean tryCastSpell()
    {
        boolean selected = selectedSpell != null;

        if (!selected)
        {
            if(selectedSpellIndex <= INVALID_SPELL_INDEX)
            {
                return false;
            }

            if (selectedSpellIndex >= spellList.size())
            {
                return false;
            }

            selected = trySelectSpell(selectedSpellIndex);
            assert selected;
        }

        assert selectedSpell != null;
        selectedSpell.onCast();
        serverRemote.send(ServerboundCastSpell.INSTANCE);

        return true;
    }

    public boolean trySelectAndCastSpell(int index)
    {
        if (trySelectSpell(index))
        {
            return tryCastSpell();
        }

        return false;
    }

    private LocalPlayer getPlayer()
    {
        var player = Minecraft.getInstance().player;
        assert player != null;

        return player;
    }

    private void initNetworkEvent()
    {
        clientNetworking.registerGlobalReceiver(
            ClientboundSpellSync.TYPE,
            (payload, ctx) ->
            {
                var syncCtx = new ClientSpellEvent.SpellSyncContext(
                    (LocalPlayer) ctx.getPlayer(),
                    payload.spellSelectIndex(),
                    payload.requireCacheCleanUp(),
                    payload.spellRecords()
                );

                ClientSpellEvent.SPELL_SYNC.fire(syncCtx);
            }
        );
    }

    private void resetCache(
        Registry<SpellType<?>> registry,
        ProblemReporter.Collector reporter,
        Collection<SpellSyncRecord> spellSyncRecords
    )
    {
        cleanUpCache();

        for (SpellSyncRecord syncRecord : spellSyncRecords)
        {
            AbstractSpell newSpell = syncRecord.constructSpell(reporter, registry, getPlayer());
            spellList.add(newSpell);
        }
    }

    private void updateCache(
        Registry<SpellType<?>> registry,
        ProblemReporter.Collector reporter,
        Collection<SpellSyncRecord> spellSyncRecords
    )
    {
        int index = 0;
        for (SpellSyncRecord syncRecord : spellSyncRecords)
        {
            boolean isNewSpell = index >= spellList.size();

            if (isNewSpell)
            {
                AbstractSpell newSpell = syncRecord.constructSpell(reporter, registry, getPlayer());
                spellList.add(newSpell);
            }
            else
            {
                AbstractSpell spell = spellList.get(index);
                boolean isSameSpell = spell.getIdentifier().equals(syncRecord.identifier());

                if (isSameSpell)
                {
                    syncRecord.syncSpell(reporter, spell);
                }
                else
                {
                    AbstractSpell newSpell = syncRecord.constructSpell(reporter, registry, getPlayer());
                    spellList.add(index, newSpell);
                }
            }

            index++;
        }
    }

    private void onTick(PlayerEntityEvent.TickContext context)
    {
        if (context.isPlayerLocal())
        {
            for (AbstractSpell spell : spellList)
            {
                spell.onTick();
            }
        }
    }

    private void onSpellSync(ClientSpellEvent.SpellSyncContext event)
    {
        var registry = Spells.getRegistry();
        ProblemReporter.Collector reporter = new ProblemReporter.Collector();
        Collection<SpellSyncRecord> spellSyncRecords = event.getSpellSyncList();

        if (event.isNeedNewCache())
        {
            resetCache(registry, reporter, spellSyncRecords);
        }
        else
        {
            updateCache(registry, reporter, spellSyncRecords);
        }

        selectedSpell = spellList.get(event.getSelectedSpellIndex());
    }
}
