package io.pastework.gradle.dsl.serializable;

import io.pastework.gradle.dsl.PlatformType;
import io.pastework.gradle.dsl.TargetSide;
import org.gradle.api.Named;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.Input;
import org.jspecify.annotations.NonNull;

import javax.inject.Inject;
import java.util.Set;

/**
 * Mixin information to be used for metadata generation.
 */
public abstract class MixinMetadata implements Named
{
    private final String name;

    @Inject
    public MixinMetadata(String name)
    {
        this.name = name;
        getTargetSide().convention(TargetSide.ANY);
        getTargetPlatform().convention(Set.of(PlatformType.values()));
    }

    /**
     * Return the config/name that will be written to metadata.
     */
    @Input
    @NonNull
    public final String getName()
    {
        return name;
    }

    /**
     * Determine which side that the mixin should be loaded. Only applicable in Fabric.
     * <p>
     * By default, this property will target every platform.
     */
    @Input
    public abstract Property<TargetSide> getTargetSide();

    /**
     * Determine which platform/loader that the mixin should be loaded from.
     * <p>
     * By default, this property will target every platform.
     */
    @Input
    public abstract SetProperty<PlatformType> getTargetPlatform();
}
