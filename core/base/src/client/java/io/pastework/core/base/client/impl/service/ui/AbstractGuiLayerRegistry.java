package io.pastework.core.base.client.impl.service.ui;

import io.pastework.core.api.client.service.ui.IGuiLayerRegistry;
import io.pastework.core.api.client.service.ui.IGuiLayerRenderable;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.resources.Identifier;

import java.util.HashSet;
import java.util.Set;

public class AbstractGuiLayerRegistry implements IGuiLayerRegistry
{
    @Getter(AccessLevel.PROTECTED)
    private final Set<GuiRegistryEntry> hudRegistrySets = new HashSet<>();
    private volatile boolean isRegistrationFinalized;

    @Override
    public boolean isRegistrationFinalized()
    {
        return isRegistrationFinalized;
    }

    protected void finalizeRegistration()
    {
        isRegistrationFinalized = true;
        hudRegistrySets.clear();
    }

    @Override
    public void registerAfter(Identifier targetLayerId, Identifier layerId, IGuiLayerRenderable hudRenderable)
    {
        register(GuiRegistryEntry.after(targetLayerId, layerId, hudRenderable));
    }

    @Override
    public void registerBelow(Identifier targetLayerId, Identifier id, IGuiLayerRenderable hudRenderable)
    {
        register(GuiRegistryEntry.below(targetLayerId, id, hudRenderable));
    }

    @Override
    public void registerReplace(Identifier targetLayerId, IGuiLayerRenderable hudRenderable)
    {
        register(GuiRegistryEntry.replace(targetLayerId, hudRenderable));
    }

    @Override
    public void registerAboveAll(Identifier layerId, IGuiLayerRenderable hudRenderable)
    {
        register(GuiRegistryEntry.aboveAll(layerId, hudRenderable));
    }

    @Override
    public void registerBelowAll(Identifier layerId, IGuiLayerRenderable hudRenderable)
    {
        register(GuiRegistryEntry.belowAll(layerId, hudRenderable));
    }

    private void register(GuiRegistryEntry entry)
    {
        synchronized (hudRegistrySets)
        {
            hudRegistrySets.add(entry);
        }
    }
}
