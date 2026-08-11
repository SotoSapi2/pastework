package io.pastework.core.fabric.common.mixin.world.extension;

import io.pastework.core.fabric.common.impl.service.attachment.IFabricAttachableExtension;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Level.class)
public class FabricLevelAttachableExtension implements IFabricAttachableExtension
{
    @Override
    public AttachmentTarget getAttachmentTarget()
    {
        return (Level) (Object) this;
    }
}
