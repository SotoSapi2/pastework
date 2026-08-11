package io.pastework.test.common.registry;

import io.pastework.core.api.Pastework;
import io.pastework.core.api.common.service.attachment.PasteworkAttachmentType;
import io.pastework.core.api.common.service.registry.ICommonRegistry;
import io.pastework.test.common.PasteworkTest;
import lombok.experimental.UtilityClass;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

@UtilityClass
public final class TestRegistries
{
    public static final ResourceKey<Registry<PasteworkAttachmentType<?>>> TEST_TYPE = ResourceKey.createRegistryKey(
        Identifier.fromNamespaceAndPath(PasteworkTest.MOD_ID, "test")
    );

    public static void initialize(ICommonRegistry registryService)
    {
        registryService.enqueueNewRegistry(TEST_TYPE);
    }
}
