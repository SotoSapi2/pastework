package io.pastework.core.neoforge.mixin.world.extension;

import io.pastework.core.neoforge.impl.service.common.attachment.INeoAttachableExtension;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Level.class)
public class NeoLevelAttachableExtension implements INeoAttachableExtension
{
    @Override
    public IAttachmentHolder getAttachmentHolder()
    {
        return (Level) (Object) this;
    }
}
