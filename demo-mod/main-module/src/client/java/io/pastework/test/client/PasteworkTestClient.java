package io.pastework.test.client;

import io.pastework.spi.IClientEntrypoint;
import io.pastework.test.client.controller.ClientSorceryController;
import io.pastework.test.client.controller.ui.SorceryHUDController;
import io.pastework.test.client.render.entity.EntityRenderRegistrar;
import lombok.Getter;

public final class PasteworkTestClient implements IClientEntrypoint
{
    @Getter
    private final ClientSorceryController clientSorceryController = new ClientSorceryController();

    @Getter
    private final SorceryHUDController sorceryHUDController = new SorceryHUDController(clientSorceryController);

    @Override
    public void clientMain()
    {
        EntityRenderRegistrar.initialize();
        PasteworkTestKeybinds.initialize();

        clientSorceryController.initialize();
        sorceryHUDController.initialize();
    }
}
