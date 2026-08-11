package io.pastework.core.api.common.service.attachment;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Represents the definition of a custom attachment type, containing its properties, codecs, and synchronization behavior.
 *
 * @param <T> The data type stored by this attachment type.
 */
@Getter
@ApiStatus.Experimental
public final class PasteworkAttachmentType<T>
{
    private final Supplier<T> initializer;
    private final @Nullable Codec<T> persistentCodec;
    private final @Nullable StreamCodec<? super ByteBuf, T> clientSyncCodec;
    private final IAttachmentSyncPredicate clientSyncPredicate;
    private final boolean copyOnDeath;

    /**
     * Checks if this attachment type supports client synchronization.
     *
     * @return {@code true} if a synchronization codec is present, {@code false} otherwise.
     */
    public boolean isSynced()
    {
        return clientSyncCodec != null;
    }

    /**
     * Checks if this attachment type is persistent (saved to disk).
     *
     * @return {@code true} if a persistent codec is present, {@code false} otherwise.
     */
    public boolean isPersistent()
    {
        return persistentCodec != null;
    }

    /**
     * Creates a new builder for constructing a {@link PasteworkAttachmentType}.
     *
     * @param initializer A supplier providing the initial default value for the attachment.
     * @param <V>         The underlying data type.
     * @return A new builder instance.
     */
    public static <V> Builder<V> builder(Supplier<V> initializer)
    {
        return new Builder<>(initializer);
    }

    /**
     * Creates the initial default value for the attachment type.
     *
     * @return The initial value.
     */
    public T createInitialValue()
    {
        return initializer.get();
    }

    /**
     * Builder for creating instances of {@link PasteworkAttachmentType}.
     *
     * @param <T> The underlying data type.
     */
    public static final class Builder<T>
    {
        private final Supplier<T> initializer;

        @Nullable Codec<T> presistentCodec;
        @Nullable StreamCodec<? super ByteBuf, T> clientSyncCodec;
        @Nullable IAttachmentSyncPredicate clientSyncPredicate;
        private boolean copyOnDeath = false;

        private Builder(Supplier<T> initializer)
        {
            this.initializer = initializer;
        }

        /**
         * Sets the codec used to save and load the attachment data persistently.
         *
         * @param codec The codec defining serialization mapping.
         * @return This builder instance for chaining.
         */
        public Builder<T> withPersistent(Codec<T> codec)
        {
            this.presistentCodec = codec;
            return this;
        }

        /**
         * Assures that this attachment should be synced to matching clients.
         *
         * @param codec               The network stream codec for serialization.
         * @param clientSyncPredicate The predicate used to determine which clients should receive updates.
         * @return This builder instance for chaining.
         */
        public Builder<T> withClientSync(
            StreamCodec<? super ByteBuf, T> codec,
            IAttachmentSyncPredicate clientSyncPredicate
        )
        {
            this.clientSyncCodec = codec;
            this.clientSyncPredicate = clientSyncPredicate;
            return this;
        }

        /**
         * Requests that this attachment should be persisted when a player respawns or when a living entity is converted.
         * <p>
         * Requires a persistent codec. Otherwise, will be ignored.
         *
         * @return current builder instance for chaining.
         */
        public Builder<T> shouldCopyOnDeath()
        {
            this.copyOnDeath = true;
            return this;
        }

        /**
         * Builds the resulting attachment type.
         *
         * @return A newly constructed {@link PasteworkAttachmentType} instance based on builder properties.
         */
        public PasteworkAttachmentType<T> build()
        {
            return new PasteworkAttachmentType<>(
                initializer,
                presistentCodec,
                clientSyncCodec,
                clientSyncPredicate,
                copyOnDeath
            );
        }
    }

    private PasteworkAttachmentType(
        Supplier<T> initializer,
        @Nullable Codec<T> persistentCodec,
        @Nullable StreamCodec<? super ByteBuf, T> clientSyncCodec,
        @Nullable IAttachmentSyncPredicate clientSyncPredicate,
        boolean copyOnDeath
    )
    {
        this.initializer = initializer;
        this.persistentCodec = persistentCodec;
        this.clientSyncCodec = clientSyncCodec;
        this.clientSyncPredicate = clientSyncPredicate != null ? clientSyncPredicate : IAttachmentSyncPredicate.ALL;

        this.copyOnDeath = copyOnDeath;
    }
}
