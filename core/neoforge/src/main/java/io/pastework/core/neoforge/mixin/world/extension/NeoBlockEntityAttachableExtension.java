package io.pastework.core.neoforge.mixin.world.extension;

import io.pastework.core.neoforge.impl.service.common.attachment.INeoAttachableExtension;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public class NeoBlockEntityAttachableExtension implements INeoAttachableExtension
{
    @Override
    public IAttachmentHolder getAttachmentHolder()
    {
        return (BlockEntity) (Object) this;
    }
}
