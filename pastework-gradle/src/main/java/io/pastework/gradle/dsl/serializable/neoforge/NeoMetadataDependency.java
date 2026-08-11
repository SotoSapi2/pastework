package io.pastework.gradle.dsl.serializable.neoforge;

import io.pastework.gradle.dsl.TargetSide;
import org.gradle.api.Named;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.jspecify.annotations.NonNull;

import javax.inject.Inject;

public abstract class NeoMetadataDependency implements Named
{
    private final String name;

    public enum Type
    {
        REQUIRED,
        OPTIONAL,
        INCOMPATIBLE,
        DISCOURAGED
    }

    public enum Ordering
    {
        NONE,
        BEFORE,
        AFTER
    }

    @Inject
    public NeoMetadataDependency(String name)
    {
        this.name = name;
        this.getVersion().convention("");
        this.getType().convention(Type.REQUIRED);
        this.getOrdering().convention(Ordering.NONE);
        this.getSide().convention(TargetSide.ANY);
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

    @Input
    public abstract Property<Ordering> getOrdering();

    @Input
    public abstract Property<TargetSide> getSide();
}
