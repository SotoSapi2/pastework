package io.pastework.core.fabric.client.impl.spi;

import io.pastework.core.api.client.service.keymapping.IKeyMappingRegistry;
import io.pastework.core.api.client.service.network.IClientNetworking;
import io.pastework.core.api.client.service.render.IEntityRendererRegistry;
import io.pastework.core.api.client.service.ui.IGuiLayerRegistry;
import io.pastework.core.api.common.service.network.INetworkRegistry;
import io.pastework.core.fabric.client.impl.internal.FabricClientEventRegistrator;
import io.pastework.core.fabric.client.impl.service.keymapping.FabricKeyMappingRegistry;
import io.pastework.core.fabric.client.impl.service.network.FabricClientNetworking;
import io.pastework.core.fabric.client.impl.service.network.FabricPlayServerRemote;
import io.pastework.core.fabric.client.impl.service.render.FabricEntityRendererRegistry;
import io.pastework.core.fabric.client.impl.service.ui.FabricGuiLayerRegistry;
import io.pastework.spi.IClientServiceRegistrator;
import io.pastework.spi.IServiceRegistryMediator;

public final class FabricClientServiceRegistrator implements IClientServiceRegistrator
{
    @Override
    public void prepareClientServices(IServiceRegistryMediator mediator)
    {
        final var keyMappingRegistry = new FabricKeyMappingRegistry();
        final var entityRendererRegistry = new FabricEntityRendererRegistry();
        final var guiLayerRegistry = new FabricGuiLayerRegistry();
        final var clientNetworking = new FabricClientNetworking(
            new FabricPlayServerRemote(),
            mediator.getService(INetworkRegistry.class)
        );

        mediator.registerService(IKeyMappingRegistry.class, keyMappingRegistry);
        mediator.registerService(IEntityRendererRegistry.class, entityRendererRegistry);
        mediator.registerService(IGuiLayerRegistry.class, guiLayerRegistry);
        mediator.registerService(IClientNetworking.class, clientNetworking);

        FabricClientEventRegistrator.initialize();
    }
}
