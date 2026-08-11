package io.pastework.core.fabric.common.mixin.world.extension;

import io.pastework.core.fabric.common.impl.service.attachment.IFabricAttachableExtension;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkAccess.class)
public class FabricChunkAttachableExtension implements IFabricAttachableExtension
{
    @Override
    public AttachmentTarget getAttachmentTarget()
    {
        return (ChunkAccess) (Object) this;
    }
}
