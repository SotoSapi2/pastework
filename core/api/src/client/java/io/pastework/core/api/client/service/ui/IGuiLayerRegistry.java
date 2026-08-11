package io.pastework.core.api.client.service.ui;

import io.pastework.core.api.Pastework;
import io.pastework.spi.IPasteworkService;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface IGuiLayerRegistry extends IPasteworkService
{
    static IGuiLayerRegistry getService()
    {
        return Pastework.INSTANCE.getService(IGuiLayerRegistry.class);
    }

    boolean isRegistrationFinalized();

    void registerAfter(
        Identifier targetLayerId,
        Identifier layerId,
        IGuiLayerRenderable hudRenderable
    );

    void registerBelow(
        Identifier targetLayerId,
        Identifier layerId,
        IGuiLayerRenderable hudRenderable
    );

    void registerReplace(
        Identifier targetLayerId,
        IGuiLayerRenderable hudRenderable
    );

    void registerAboveAll(
        Identifier layerId,
        IGuiLayerRenderable hudRenderable
    );

    void registerBelowAll(
        Identifier layerId,
        IGuiLayerRenderable hudRenderable
    );
}
