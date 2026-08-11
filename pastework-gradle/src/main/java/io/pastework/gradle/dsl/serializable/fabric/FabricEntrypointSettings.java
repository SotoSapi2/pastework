package io.pastework.gradle.dsl.serializable.fabric;

import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

import java.io.Serializable;

public abstract class FabricEntrypointSettings implements Serializable
{
    @Input
    @Optional
    public abstract ListProperty<String> getMainEntrypoints();

    @Input
    @Optional
    public abstract ListProperty<String> getClientEntrypoints();

    @Input
    @Optional
    public abstract ListProperty<String> getPreLaunchEntrypoints();
}
