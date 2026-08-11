package io.pastework.core.api.common.service.attachment;

import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiPredicate;

/**
 * Predicate used to determine if an attachment should be synchronized to a specific player.
 */
@FunctionalInterface
public interface IAttachmentSyncPredicate extends BiPredicate<IAttachmentHolderAdapter, ServerPlayer>
{
    /**
     * Predicate that only synchronizes the attachment data to the player they belong to.
     */
    IAttachmentSyncPredicate SELF = (target, player) -> target.is(player);

    /**
     * Predicate that synchronizes the attachment data to all tracking players.
     */
    IAttachmentSyncPredicate ALL = (target, player) -> true;
}
