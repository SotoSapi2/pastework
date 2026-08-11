package io.pastework.test.common.registry;

import io.pastework.core.api.common.service.attachment.IAttachmentSyncPredicate;
import io.pastework.core.api.common.service.attachment.PasteworkAttachmentType;
import io.pastework.core.api.common.service.attachment.IAttachmentRegistrar;
import io.pastework.core.api.common.service.attachment.IAttachmentRegistry;
import io.pastework.test.common.PasteworkTest;
import io.pastework.test.common.attachment.ManaAttachment;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Attachments
{
    public static final IAttachmentRegistrar HOLDER = IAttachmentRegistrar.create(PasteworkTest.MOD_ID);

    public static final PasteworkAttachmentType<ManaAttachment> MANA = HOLDER.register(
        "mana",
        PasteworkAttachmentType.builder(ManaAttachment::createDefault)
            .withPersistent(ManaAttachment.CODEC)
            .withClientSync(ManaAttachment.STREAM_CODEC, IAttachmentSyncPredicate.SELF)
            .build()
    );

    public static void initialize(IAttachmentRegistry attachmentRegistry)
    {
        attachmentRegistry.enqueueRegistrar(HOLDER);
    }
}
