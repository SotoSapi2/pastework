package io.pastework.test.common;

import io.pastework.core.api.common.service.attachment.IAttachmentRegistry;
import io.pastework.core.api.common.service.registry.ICommonRegistry;
import io.pastework.spi.ICommonEntrypoint;
import io.pastework.test.common.controller.ServerSorceryController;
import io.pastework.test.common.network.PacketRegistrar;
import io.pastework.test.common.registry.Attachments;
import io.pastework.test.common.registry.ModEntities;
import io.pastework.test.common.registry.Spells;
import io.pastework.test.common.registry.TestRegistries;
import lombok.Getter;

public final class PasteworkTest implements ICommonEntrypoint
{
    public static final String MOD_ID = "pastework_test";

    @Getter
    private final ServerSorceryController serverSorceryController = new ServerSorceryController();

    @Override
    public void commonMain()
    {
        ICommonRegistry registryService = ICommonRegistry.getService();
        IAttachmentRegistry attachmentRegistry = IAttachmentRegistry.getService();

        TestRegistries.initialize(registryService);
        ModEntities.initialize();
        Attachments.initialize(attachmentRegistry);
        Spells.initialize();
        PacketRegistrar.initialize();

        serverSorceryController.initialize();
    }
}
