package io.pastework.core.fabric.common.mixin.world.extension;


import io.pastework.core.fabric.common.impl.service.attachment.IFabricAttachableExtension;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public class FabricBlockEntityAttachableExtension implements IFabricAttachableExtension
{
    @Override
    public AttachmentTarget getAttachmentTarget()
    {
        return (BlockEntity) (Object) this;
    }
}
