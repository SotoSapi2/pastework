package io.pastework.core.fabric.common.mixin.world.extension;

import io.pastework.core.fabric.common.impl.service.attachment.IFabricAttachableExtension;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class FabricEntityAttachableExtension implements IFabricAttachableExtension
{
    @Override
    public AttachmentTarget getAttachmentTarget()
    {
        return (Entity) (Object) this;
    }
}
