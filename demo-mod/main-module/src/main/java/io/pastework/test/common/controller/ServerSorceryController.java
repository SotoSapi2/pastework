package io.pastework.test.common.controller;

import io.pastework.core.api.common.event.IEventConnection;
import io.pastework.core.api.common.event.lifecycle.BootstrapEvent;
import io.pastework.core.api.common.event.world.PlayerEntityEvent;
import io.pastework.core.api.common.event.world.ServerPlayerEvent;
import io.pastework.core.api.common.service.attachment.IAttachableExtension;
import io.pastework.core.api.common.service.network.IPlayClientRemote;
import io.pastework.core.api.common.service.network.IServerNetworking;
import io.pastework.test.common.attachment.ManaAttachment;
import io.pastework.test.common.event.ServerPlayerSpellEvent;
import io.pastework.test.common.network.clientbound.play.spell.ClientboundSpellSync;
import io.pastework.test.common.network.serverbound.play.spell.ServerboundCastSpell;
import io.pastework.test.common.network.serverbound.play.spell.ServerboundSelectSpell;
import io.pastework.test.common.registry.Attachments;
import io.pastework.test.common.registry.Spells;
import io.pastework.test.common.spell.AbstractSpell;
import io.pastework.test.common.spell.ISpellContainerHolder;
import io.pastework.test.common.spell.SpellSyncRecord;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ServerSorceryController
{
    private final IServerNetworking serverNetworking = IServerNetworking.getService();
    private final IPlayClientRemote clientRemote = IPlayClientRemote.getRemote();
    private final List<IEventConnection> eventConnectionList = new ArrayList<>();

    public void initialize()
    {
        eventConnectionList.add(ServerPlayerEvent.JOIN.connect(this::onPlayerJoin));
        eventConnectionList.add(ServerPlayerEvent.RESPAWN.connect(this::onPlayerSpawn));
        eventConnectionList.add(PlayerEntityEvent.PRE_TICK.connect(this::onPlayerTick));
        eventConnectionList.add(ServerPlayerSpellEvent.SELECT_REQUEST.connect(this::onSelectRequest));
        eventConnectionList.add(ServerPlayerSpellEvent.CAST_REQUEST.connect(this::onCastRequest));
        eventConnectionList.add(BootstrapEvent.COMMON_SETUP.connect(this::openNetworkEvent));
    }

    public void close()
    {
        for(var connection : eventConnectionList)
        {
            connection.disconnect();
        }
        eventConnectionList.clear();
        closeNetworkEvent();
    }

    public void initializePlayerState(ServerPlayer player)
    {
        IAttachableExtension attachable = (IAttachableExtension) player;

        if (!attachable.hasAttachment(Attachments.MANA))
        {
            attachable.emplaceAttachment(Attachments.MANA);
        }

        var registry = Spells.getRegistry();
        ISpellContainerHolder spellContainer = (ISpellContainerHolder) player;
        spellContainer.getSpellList()
            .removeIf(it -> !registry.containsKey(it.getIdentifier()));

        if(spellContainer.hasNoSpell())
        {
            spellContainer.addSpell(Spells.PLASMABURST.getEntry());
            spellContainer.addSpell(Spells.POTATOBREATH.getEntry());
            spellContainer.addSpell(Spells.HEAL_BUFF.getEntry());
            spellContainer.addSpell(Spells.SPEED_BUFF.getEntry());
        }

        syncPlayerSpells(
            player,
            spellContainer.getSelectedSpellIndex(),
            true
        );
    }

    public void syncPlayerSpells(ServerPlayer player, int selectedSpellIndex, boolean invalidateCache)
    {
        ProblemReporter.Collector reporter = new ProblemReporter.Collector();
        ISpellContainerHolder spellContainer = (ISpellContainerHolder) player;
        Collection<SpellSyncRecord> syncRecordList = SpellSyncRecord.fromHolder(reporter, spellContainer);

        var packet = new ClientboundSpellSync(selectedSpellIndex, invalidateCache, syncRecordList);
        clientRemote.sendTo(player, packet);
    }

    private void openNetworkEvent()
    {
        serverNetworking.registerGlobalReceiver(
            ServerboundCastSpell.TYPE,
            (payload, ctx) ->
            {
                var castCtx = new ServerPlayerSpellEvent.CastRequestContext(
                    (ServerPlayer) ctx.getPlayer()
                );

                ServerPlayerSpellEvent.CAST_REQUEST.fire(castCtx);
            }
        );

        serverNetworking.registerGlobalReceiver(
            ServerboundSelectSpell.TYPE,
            (payload, ctx) ->
            {
                var selectCtx = new ServerPlayerSpellEvent.SelectRequestContext(
                    (ServerPlayer) ctx.getPlayer(),
                    payload.spellIndex()
                );

                ServerPlayerSpellEvent.SELECT_REQUEST.fire(selectCtx);
            }
        );
    }

    private void closeNetworkEvent()
    {
        serverNetworking.unregisterGlobalReceiver(ServerboundCastSpell.TYPE);
        serverNetworking.unregisterGlobalReceiver(ServerboundSelectSpell.TYPE);
    }

    private void onPlayerJoin(ServerPlayer player)
    {
        initializePlayerState(player);
    }

    private void onPlayerSpawn(ServerPlayerEvent.RespawnContext context, ServerPlayer player)
    {
        initializePlayerState(player);
    }

    private void onPlayerTick(PlayerEntityEvent.TickContext context)
    {
        if(context.getPlayer() instanceof ServerPlayer serverPlayer)
        {
            ISpellContainerHolder spellContainer = (ISpellContainerHolder) serverPlayer;
            IAttachableExtension attachable = (IAttachableExtension) serverPlayer;

            attachable.editOrEmplaceAttachment(
                Attachments.MANA,
                ManaAttachment::replenishByRegenRate
            );

            for(AbstractSpell spell : spellContainer.getSpellList())
            {
                spell.onTick();
            }
        }
    }

    private void onSelectRequest(ServerPlayerSpellEvent.SelectRequestContext event)
    {
        ISpellContainerHolder spellContainer = (ISpellContainerHolder) event.getPlayer();

        if(!spellContainer.isSpellIndexValid(event.getSpellIndex()))
        {
            event.disconnectPlayer("Invalid spell index selection.");
        }

        if(spellContainer.getSelectedSpellIndex() != event.getSpellIndex())
        {
            spellContainer.setSelectedSpellIndex(event.getSpellIndex());
            AbstractSpell selectedSpell = spellContainer.getSelectedSpell();

            selectedSpell.onSelect();
        }
    }

    private void onCastRequest(ServerPlayerSpellEvent.CastRequestContext event)
    {
        ISpellContainerHolder spellContainer = (ISpellContainerHolder) event.getPlayer();

        if(spellContainer.hasNoSpell())
        {
            return;
        }

        if(!spellContainer.isSelectedSpellIndexValid())
        {
            event.disconnectPlayer("Invalid spell index selection.");
        }

        AbstractSpell selectedSpell = spellContainer.getSelectedSpell();

        if(selectedSpell.isSpellCanBeUsed())
        {
            selectedSpell.onCast();
            event.getPlayer()
                .swing(InteractionHand.MAIN_HAND, true);
        }
    }
}
