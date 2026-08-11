package io.pastework.gradle.dsl.serializable;

import io.pastework.gradle.dsl.PlatformType;
import io.pastework.gradle.dsl.serializable.fabric.FabricMetadataSettings;
import io.pastework.gradle.dsl.serializable.neoforge.MetadataDependencySettings;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.Optional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Set;

/**
 * DSL settings to be used for metadata/mod files generation.
 */
public abstract class MetadataSettings implements Serializable
{
    public static final String DEFAULT_LICENSE = "All rights reserved";
    private final FabricMetadataSettings fabricMetadataSettings;
    private final MetadataDependencySettings metadataDependencySettings;

    /**
     * The identifier of the mod.
     * This property can only have lowercase character, digits and underscore.
     * <p>
     * This property is required to be set to generate the metadata.
     */
    @Input
    public abstract Property<String> getModId();

    /**
     * The version of the mod.
     * This property format must follow <a href="https://semver.org/">Semantic Versioning</a>.
     * <p>
     * This property is required to be set to generate the metadata.
     */
    @Input
    public abstract Property<String> getModVersion();

    /**
     * The friendly name that would be displayed to the user.
     * <p>
     * This property is required to be set to generate the metadata.
     */
    @Input
    public abstract Property<String> getDisplayName();

    /**
     * This license of the mod.
     * <p>
     * By default, this property will be set as {@code "All rights reversed"}
     */
    @Input
    public abstract Property<String> getLicense();

    @Input
    @Optional
    public abstract Property<String> getDescription();

    @Input
    @Optional
    public abstract ListProperty<String> getAuthors();

    /**
     * Returns dependency settings to manage mod dependency.
     */
    @Nested
    public final MetadataDependencySettings getDependencies()
    {
        return metadataDependencySettings;
    }

    /**
     * Configure dependency settings to manage mod dependency.
     */
    public final void dependencies(Action<MetadataDependencySettings> action)
    {
        action.execute(metadataDependencySettings);
    }

    @Nested
    @Optional
    public abstract NamedDomainObjectContainer<MixinMetadata> getMixins();

    /**
     * Determine if metadata files should be generated.
     * <p>
     * By default, this property is set as {@code true}.
     */
    @Input
    @Optional
    public abstract Property<Boolean> getShouldGenerate();

    /**
     * Determine which platforms' metadata file will be generated.
     * <p>
     * By default, this property will target every platform.
     */
    @Input
    @Optional
    public abstract SetProperty<PlatformType> getTargetPlatforms();

    /**
     * Return the metadata settings that's exclusive to Fabric platform.
     */
    @Nested
    public final FabricMetadataSettings getFabric()
    {
        return fabricMetadataSettings;
    }

    /**
     * Configure the metadata settings that's exclusive to Fabric platform.
     */
    public final void fabric(Action<FabricMetadataSettings> action)
    {
        action.execute(fabricMetadataSettings);
    }

    @Inject
    public MetadataSettings(ObjectFactory objectFactory)
    {
        this.fabricMetadataSettings = objectFactory.newInstance(FabricMetadataSettings.class);
        this.metadataDependencySettings = objectFactory.newInstance(MetadataDependencySettings.class);
        this.getShouldGenerate().convention(true);
        this.getTargetPlatforms().convention(Set.of(PlatformType.values()));
        this.getLicense().convention(DEFAULT_LICENSE);
    }
}
