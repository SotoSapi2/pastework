package io.pastework.gradle.dsl.serializable.fabric;

import org.gradle.api.Named;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.jspecify.annotations.NonNull;

import javax.inject.Inject;

public abstract class FabricMetadataDependency implements Named
{
    private final String name;

    public enum Type
    {
        DEPENDS,
        RECOMMENDS,
        SUGGESTS,
        CONFLICTS,
        BREAKS
    }

    @Inject
    public FabricMetadataDependency(String name)
    {
        this.name = name;
        this.getType().convention(Type.DEPENDS);
        this.getVersion().convention("*");
    }

    @Input
    @Override
    public @NonNull String getName()
    {
        return name;
    }

    @Input
    public abstract Property<Type> getType();

    @Input
    public abstract Property<String> getVersion();
}
