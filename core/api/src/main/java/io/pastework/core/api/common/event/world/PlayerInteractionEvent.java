package io.pastework.core.api.common.event.world;

import io.pastework.core.api.common.event.Event1;
import io.pastework.core.api.common.event.ICancellableEventContext;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@ApiStatus.NonExtendable
public interface PlayerInteractionEvent
{
    Event1<ItemInteractContext> ITEM_INTERACT = new Event1<>();

    Event1<EntityInteractContext> ENTITY_INTERACT = new Event1<>();

    Event1<BlockAttackContext> BLOCK_ATTACK = new Event1<>();

    Event1<BlockDamageContext> BLOCK_DAMAGE = new Event1<>();

    Event1<BlockInteractContext> BLOCK_INTERACT = new Event1<>();

    enum BlockAction
    {
        /**
         * When the player first left-clicks a block
         */
        START,
        /**
         * When the player stops left-clicking a block by completely breaking it
         */
        STOP,
        /**
         * When the player stops left-clicking a block by releasing the button, or no
         * longer targeting the same block before it breaks.
         */
        ABORT;

        public static BlockAction fromPacketAction(ServerboundPlayerActionPacket.Action action)
        {
            return switch (action)
            {
                case START_DESTROY_BLOCK -> START;
                case STOP_DESTROY_BLOCK -> STOP;
                case ABORT_DESTROY_BLOCK -> ABORT;
                default -> throw new AssertionError();
            };
        }
    }

    @Getter
    abstract class AbstractContext extends AbstractWorldEventContext
    {
        private final Player player;

        public AbstractContext(Player player)
        {
            super(player.level());
            this.player = player;
        }

        public boolean isPlayerLocal()
        {
            return player.isLocalPlayer();
        }
    }

    @Getter
    abstract class AbstractBlockContext extends AbstractContext implements ICancellableEventContext
    {
        @Setter
        private boolean cancelled;
        private final ItemStack stack;
        private final InteractionHand hand;
        private final BlockPos blockPos;

        public AbstractBlockContext(Player player, ItemStack stack, InteractionHand hand, BlockPos blockPos)
        {
            super(player);
            this.stack = stack;
            this.hand = hand;
            this.blockPos = blockPos;
        }

        public BlockState getBlockState()
        {
            return getLevel().getBlockState(blockPos);
        }
    }

    final class ItemInteractContext extends AbstractContext implements ICancellableEventContext
    {
        @Getter
        @Setter
        private boolean cancelled;

        @Getter
        private final ItemStack stack;

        @Getter
        private final InteractionHand hand;

        @Setter
        private @Nullable InteractionResult interactionResult;

        @ApiStatus.Internal
        public ItemInteractContext(Player player, ItemStack stack, InteractionHand hand)
        {
            super(player);
            this.stack = stack;
            this.hand = hand;
        }

        public boolean isInteractionResultEdited()
        {
            return interactionResult != null;
        }

        public Optional<InteractionResult> getEditedInteractionResult()
        {
            return Optional.ofNullable(interactionResult);
        }
    }

    final class EntityInteractContext extends AbstractContext implements ICancellableEventContext
    {
        @Getter
        @Setter
        private boolean cancelled;

        @Getter
        private final Entity entity;

        @Getter
        private final InteractionHand hand;

        @Setter
        private @Nullable InteractionResult interactionResult;

        @ApiStatus.Internal
        public EntityInteractContext(Player player, Entity entity, InteractionHand hand)
        {
            super(player);
            this.entity = entity;
            this.hand = hand;
        }

        public boolean isInteractionResultEdited()
        {
            return interactionResult != null;
        }

        public Optional<InteractionResult> getEditedInteractionResult()
        {
            return Optional.ofNullable(interactionResult);
        }
    }

    final class BlockAttackContext extends AbstractBlockContext
    {
        @Getter
        private final BlockAction action;

        @ApiStatus.Internal
        public BlockAttackContext(Player player, ItemStack stack, InteractionHand hand, BlockAction action, BlockPos blockPos)
        {
            super(player, stack, hand, blockPos);
            this.action = action;
        }
    }

    final class BlockDamageContext extends AbstractBlockContext
    {
        @Getter
        private final BlockAction action;

        @ApiStatus.Internal
        public BlockDamageContext(Player player, ItemStack stack, InteractionHand hand, BlockAction action, BlockPos blockPos)
        {
            super(player, stack, hand, blockPos);
            this.action = action;
        }
    }

    final class BlockInteractContext extends AbstractBlockContext
    {
        @Setter
        private @Nullable InteractionResult interactionResult;

        @ApiStatus.Internal
        public BlockInteractContext(Player player, ItemStack stack, InteractionHand hand, BlockPos blockPos)
        {
            super(player, stack, hand, blockPos);
        }

        public boolean isInteractionResultEdited()
        {
            return interactionResult != null;
        }

        public Optional<InteractionResult> getEditedInteractionResult()
        {
            return Optional.ofNullable(interactionResult);
        }
    }
}
