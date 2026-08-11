package io.pastework.core.neoforge.mixin.world.extension;

import io.pastework.core.neoforge.impl.service.common.attachment.INeoAttachableExtension;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkAccess.class)
public class NeoChunkAttachableExtension implements INeoAttachableExtension
{
    @Override
    public IAttachmentHolder getAttachmentHolder()
    {
        return (ChunkAccess) (Object) this;
    }
}
