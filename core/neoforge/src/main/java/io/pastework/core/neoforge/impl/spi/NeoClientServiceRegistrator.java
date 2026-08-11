package io.pastework.core.neoforge.impl.spi;

import io.pastework.core.api.client.service.keymapping.IKeyMappingRegistry;
import io.pastework.core.api.client.service.network.IClientNetworking;
import io.pastework.core.api.client.service.render.IEntityRendererRegistry;
import io.pastework.core.api.client.service.ui.IGuiLayerRegistry;
import io.pastework.core.api.common.service.network.INetworkRegistry;
import io.pastework.core.neoforge.impl.internal.NeoClientEventRegistrator;
import io.pastework.core.neoforge.impl.service.client.keymapping.NeoKeyMappingRegistry;
import io.pastework.core.neoforge.impl.service.client.network.NeoClientNetworking;
import io.pastework.core.neoforge.impl.service.client.network.NeoPlayServerRemote;
import io.pastework.core.neoforge.impl.service.client.render.NeoEntityRendererRegistry;
import io.pastework.core.neoforge.impl.service.client.ui.NeoGuiLayerRegistry;
import io.pastework.core.neoforge.impl.service.common.network.NetworkHolderRecord;
import io.pastework.spi.IClientServiceRegistrator;
import io.pastework.spi.IServiceRegistryMediator;

public final class NeoClientServiceRegistrator implements IClientServiceRegistrator
{
    @Override
    public void prepareClientServices(IServiceRegistryMediator mediator)
    {
        final var networkRegistry = mediator.getService(INetworkRegistry.class);
        final var networkHolderRecord = mediator.getService(NetworkHolderRecord.class);

        final var keyMappingRegistry = new NeoKeyMappingRegistry();
        final var guiRegistry = new NeoGuiLayerRegistry();
        final var entityRendererRegistry = new NeoEntityRendererRegistry();
        final var clientNetworking = new NeoClientNetworking(
            new NeoPlayServerRemote(),
            networkRegistry,
            networkHolderRecord.clientHolder()
        );

        mediator.registerService(IKeyMappingRegistry.class, keyMappingRegistry);
        mediator.registerService(IGuiLayerRegistry.class, guiRegistry);
        mediator.registerService(IEntityRendererRegistry.class, entityRendererRegistry);
        mediator.registerService(IClientNetworking.class, clientNetworking);

        NeoClientEventRegistrator.initialize();
    }
}
