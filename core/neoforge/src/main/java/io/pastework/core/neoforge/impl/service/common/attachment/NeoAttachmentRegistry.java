package io.pastework.core.neoforge.impl.service.common.attachment;

import com.mojang.serialization.Codec;
import io.pastework.core.api.common.service.attachment.IAttachmentHolderAdapter;
import io.pastework.core.api.common.service.attachment.IAttachmentSyncPredicate;
import io.pastework.core.api.common.service.attachment.PasteworkAttachmentType;
import io.pastework.core.api.exception.RegistryException;
import io.pastework.core.base.common.impl.service.attachment.AbstractAttachmentRegistry;
import io.pastework.core.neoforge.impl.service.INeoEventBusDependant;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public final class NeoAttachmentRegistry extends AbstractAttachmentRegistry<AttachmentType<?>> implements
    INeoEventBusDependant
{
    @SuppressWarnings("unchecked")
    public <T> AttachmentType<T> getNeoAttachmentType(PasteworkAttachmentType<T> type)
    {
        if(!getAttachmentRegistryMap().containsKey(type))
        {
            throw new RegistryException("Passed attachment is not registered.");
        }

        return (AttachmentType<T>) getAttachmentRegistryMap().get(type);
    }

    @Override
    protected <T> void registerAttachment(Identifier key, PasteworkAttachmentType<T> type)
    {
        Supplier<T> initializer = type.getInitializer();
        AttachmentType.Builder<T> builder = AttachmentType.builder(initializer);

        if (type.isPersistent() && type.getPersistentCodec() != null)
        {
            var adapter = new AttachmentSerializerAdapter<T>(key, type.getPersistentCodec());
            builder.serialize(adapter);
        }

        if(type.isSynced() && type.getClientSyncCodec() != null)
        {
            var adapter = new AttachmentPredicateAdapter(
                this,
                type.getClientSyncPredicate()
            );
            builder.sync(adapter, type.getClientSyncCodec());
        }

        if (type.isCopyOnDeath() && type.getPersistentCodec() != null)
        {
            builder.copyOnDeath();
        }

        AttachmentType<T> attachmentType = builder.build();

        synchronized (getAttachmentRegistryMap())
        {
            Registry.register(NeoForgeRegistries.ATTACHMENT_TYPES, key, attachmentType);
            getAttachmentRegistryMap().put(type, attachmentType);
        }
    }

    @SubscribeEvent
    public void onRegistration(RegisterEvent event)
    {
        event.register(
            NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
            it -> finalizeRegistration()
        );
    }

    @Override
    public void handleEventBus(IEventBus eventBus)
    {
        eventBus.register(this);
    }

    private record AttachmentPredicateAdapter(NeoAttachmentRegistry registry, IAttachmentSyncPredicate predicate)
        implements BiPredicate<IAttachmentHolder, ServerPlayer>
    {
        @Override
        public boolean test(IAttachmentHolder attachmentTarget, ServerPlayer player)
        {
            var view = new AttachmentHolderView(
                registry,
                attachmentTarget
            );

            return predicate.test(view, player);
        }
    }

    private record AttachmentSerializerAdapter<T>(Identifier identifier, Codec<T> codec)
        implements IAttachmentSerializer<T>
    {
        @Override
        public T read(IAttachmentHolder holder, ValueInput input)
        {
            Optional<T> parsingResult = input.read(identifier.toString(), codec);
            return parsingResult.orElseThrow();
        }

        @Override
        public boolean write(T attachment, ValueOutput output)
        {
            output.store(identifier.toString(), codec, attachment);
            return true;
        }
    }

    private record AttachmentHolderView(NeoAttachmentRegistry registry, IAttachmentHolder target)
        implements IAttachmentHolderAdapter
    {
        @Override
        public <T> T get(PasteworkAttachmentType<T> attachment) throws NoSuchElementException
        {
            if(!has(attachment))
            {
                throw new NoSuchElementException();
            }

            return target.getData(registry.getNeoAttachmentType(attachment));
        }

        @Override
        public boolean has(PasteworkAttachmentType<?> attachment)
        {
            return target.hasData(registry.getNeoAttachmentType(attachment));
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
