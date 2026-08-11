package io.pastework.core.neoforge.impl.spi;

import io.pastework.core.api.common.service.attachment.IAttachmentRegistry;
import io.pastework.core.api.common.service.network.INetworkRegistry;
import io.pastework.core.api.common.service.network.IServerNetworking;
import io.pastework.core.api.common.service.registry.ICommonRegistry;
import io.pastework.core.neoforge.impl.internal.NeoCommonEventRegistrator;
import io.pastework.core.neoforge.impl.service.common.attachment.NeoAttachmentRegistry;
import io.pastework.core.neoforge.impl.service.common.network.NeoNetworkRegistry;
import io.pastework.core.neoforge.impl.service.common.network.NeoPlayClientRemote;
import io.pastework.core.neoforge.impl.service.common.network.NeoServerNetworking;
import io.pastework.core.neoforge.impl.service.common.network.NetworkHolderRecord;
import io.pastework.core.neoforge.impl.service.common.registry.NeoCommonRegistry;
import io.pastework.spi.ICommonServiceRegistrator;
import io.pastework.spi.IServiceRegistryMediator;

public final class NeoCommonServiceRegistrator implements ICommonServiceRegistrator
{
    @Override
    public void prepareCommonServices(IServiceRegistryMediator mediator)
    {
        final var networkHolderRecord = NetworkHolderRecord.createDefault();
        final var networkRegistry = new NeoNetworkRegistry(networkHolderRecord);
        final var registryService = new NeoCommonRegistry();
        final var neoAttachmentRegistry = new NeoAttachmentRegistry();
        final var serverNetworking = new NeoServerNetworking(
            new NeoPlayClientRemote(),
            networkRegistry,
            networkHolderRecord.serverHolder()
        );

        mediator.registerService(NetworkHolderRecord.class, networkHolderRecord);
        mediator.registerService(ICommonRegistry.class, registryService);
        mediator.registerService(IServerNetworking.class, serverNetworking);
        mediator.registerService(INetworkRegistry.class, networkRegistry);
        mediator.registerService(IAttachmentRegistry.class, neoAttachmentRegistry);

        NeoCommonEventRegistrator.initialize();
    }
}
