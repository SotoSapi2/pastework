package io.pastework.core.fabric.common.impl.service.attachment;

import io.pastework.core.api.common.service.attachment.IAttachmentHolderAdapter;
import io.pastework.core.api.common.service.attachment.IAttachmentSyncPredicate;
import io.pastework.core.api.common.service.attachment.PasteworkAttachmentType;
import io.pastework.core.api.exception.RegistryException;
import io.pastework.core.base.common.impl.service.attachment.AbstractAttachmentRegistry;
import io.pastework.core.fabric.common.impl.service.IFabricRegistrable;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public final class FabricAttachmentRegistry extends AbstractAttachmentRegistry<AttachmentType<?>>
    implements
    IFabricRegistrable
{
    @SuppressWarnings("unchecked")
    public <T> AttachmentType<T> getFabricAttachmentType(PasteworkAttachmentType<T> type)
    {
        if(!getAttachmentRegistryMap().containsKey(type))
        {
            throw new RegistryException("Passed attachment is not registered.");
        }

        return (AttachmentType<T>) getAttachmentRegistryMap().get(type);
    }

    @SuppressWarnings("deprecation")
    protected <T> void registerAttachment(Identifier key, PasteworkAttachmentType<T> type)
    {
        Supplier<T> initializer = type.getInitializer();
        AttachmentRegistry.Builder<T> builder = AttachmentRegistry.<T>builder()
            .initializer(initializer);

        if (type.isPersistent() && type.getPersistentCodec() != null)
        {
            builder.persistent(type.getPersistentCodec());
        }

        if (type.isCopyOnDeath() && type.getPersistentCodec() != null)
        {
            builder.copyOnDeath();
        }

        if (type.isSynced() && type.getClientSyncCodec() != null)
        {
            var adapter = new AttachmentPredicateAdapter(
                this,
                type.getClientSyncPredicate()
            );

            builder.syncWith(type.getClientSyncCodec(), adapter);
        }

        synchronized (getAttachmentRegistryMap())
        {
            AttachmentType<T> attachmentType = builder.buildAndRegister(key);
            getAttachmentRegistryMap().put(type, attachmentType);
        }
    }

    @Override
    public boolean isRegistrationFinalized()
    {
        return super.isRegistrationFinalized();
    }

    @Override
    public void processRegistration()
    {
        if(isRegistrationFinalized())
        {
            throw new IllegalStateException();
        }

        finalizeRegistration();
    }

    private record AttachmentPredicateAdapter(FabricAttachmentRegistry registry, IAttachmentSyncPredicate predicate)
        implements AttachmentSyncPredicate
    {
        @Override
        public boolean test(AttachmentTarget attachmentTarget, ServerPlayer player)
        {
            var view = new AttachmentHolderAdapter(registry, attachmentTarget);
            return predicate.test(view, player);
        }
    }

    private record AttachmentHolderAdapter(FabricAttachmentRegistry registry, AttachmentTarget target)
        implements IAttachmentHolderAdapter
    {
        @Override
        public <T> T get(PasteworkAttachmentType<T> attachment) throws NoSuchElementException
        {
            if(!has(attachment))
            {
                throw new NoSuchElementException();
            }

            return target.getAttached(registry.getFabricAttachmentType(attachment));
        }

        @Override
        public boolean has(PasteworkAttachmentType<?> attachment)
        {
            return target.hasAttached(registry.getFabricAttachmentType(attachment));
        }

        @Override
        public boolean is(Object object)
        {
            return target == object;
        }

        @Override
        public Object getHolderInstance()
        {
            return target;
        }
    }
}
