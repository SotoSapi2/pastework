package io.pastework.core.fabric.common.impl.spi;

import io.pastework.spi.IServiceRegistryMediator;
import io.pastework.core.api.common.service.attachment.IAttachmentRegistry;
import io.pastework.core.api.common.service.network.INetworkRegistry;
import io.pastework.core.api.common.service.network.IServerNetworking;
import io.pastework.core.api.common.service.registry.ICommonRegistry;
import io.pastework.core.fabric.common.impl.internal.FabricCommonEventRegistrator;
import io.pastework.core.fabric.common.impl.service.attachment.FabricAttachmentRegistry;
import io.pastework.core.fabric.common.impl.service.network.FabricNetworkRegistry;
import io.pastework.core.fabric.common.impl.service.network.FabricPlayClientRemote;
import io.pastework.core.fabric.common.impl.service.network.FabricServerNetworking;
import io.pastework.core.fabric.common.impl.service.registry.FabricCommonRegistry;
import io.pastework.spi.ICommonServiceRegistrator;

public class FabricCommonServiceRegistrator implements ICommonServiceRegistrator
{
    @Override
    public void prepareCommonServices(IServiceRegistryMediator mediator)
    {
        final var registryService = new FabricCommonRegistry();
        final var networkRegistry = new FabricNetworkRegistry();
        final var attachmentRegistry = new FabricAttachmentRegistry();
        final var serverNetworking = new FabricServerNetworking(
            new FabricPlayClientRemote(),
            networkRegistry
        );

        mediator.registerService(ICommonRegistry.class, registryService);
        mediator.registerService(IServerNetworking.class, serverNetworking);
        mediator.registerService(INetworkRegistry.class, networkRegistry);
        mediator.registerService(IAttachmentRegistry.class, attachmentRegistry);

        FabricCommonEventRegistrator.initialize();
    }
}
