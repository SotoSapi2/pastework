package io.pastework.gradle.dsl.serializable.fabric;

import io.pastework.gradle.dsl.TargetSide;
import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.Optional;

import javax.inject.Inject;
import java.io.Serializable;

public abstract class FabricMetadataSettings implements Serializable
{
    @Input
    @Optional
    public abstract Property<String> getFabricLoaderVersion();

    @Nested
    public abstract Property<FabricEntrypointSettings> getEntrypoints();

    @Input
    public abstract Property<TargetSide> getEnvironment();

    public void entrypoints(Action<FabricEntrypointSettings> action)
    {
        action.execute(getEntrypoints().get());
    }

    @Inject
    public FabricMetadataSettings(ObjectFactory objectFactory)
    {
        getEntrypoints().convention(objectFactory.newInstance(FabricEntrypointSettings.class));
        getEnvironment().convention(TargetSide.ANY);
    }
}
