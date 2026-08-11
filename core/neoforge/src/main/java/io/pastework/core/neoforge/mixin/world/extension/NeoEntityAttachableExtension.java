package io.pastework.core.neoforge.mixin.world.extension;

import io.pastework.core.neoforge.impl.service.common.attachment.INeoAttachableExtension;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class NeoEntityAttachableExtension implements INeoAttachableExtension
{
    @Override
    public IAttachmentHolder getAttachmentHolder()
    {
        return (Entity) (Object) this;
    }
}
