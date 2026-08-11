package io.pastework.gradle.dsl;

import io.pastework.gradle.dsl.serializable.MetadataSettings;
import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

import javax.inject.Inject;

/**
 * DSL extension provided by Pastework plugin to handle platform management.
 */
public abstract class PasteworkExtension
{
    private final PlatformType platform;

    @Inject
    public PasteworkExtension(PlatformType platform, ObjectFactory objectFactory)
    {
        this.platform = platform;
        this.getMetadata().convention(objectFactory.newInstance(MetadataSettings.class));
        this.getPublishVariants().convention(objectFactory.newInstance(PublishVariantSettings.class));
        this.getShouldGenerateRunConfig().convention(false);
    }

    /**
     * Return the settings that would be used by the metadata/mod files generation.
     */
    public abstract Property<MetadataSettings> getMetadata();

    /**
     * Configures the settings that that would be used by the metadata/mod files generation.
     */
    public final void metadata(Action<MetadataSettings> action)
    {
        action.execute(getMetadata().get());
    }

    /**
     * Determine whether the plugin should generate run configurations.
     * <p>
     * This option might be deprecated in the future and replaced with more
     * sophisticated settings.
     * <p>
     * By default, this property is set to {@code false}.
     */
    public abstract Property<Boolean> getShouldGenerateRunConfig();

    public abstract Property<PublishVariantSettings> getPublishVariants();

    public final void publishVariants(Action<PublishVariantSettings> action)
    {
        action.execute(getPublishVariants().get());
    }

    /**
     * Return which platform that the plugin currently depending on.
     * <p>
     * This property is determined by {@code pastework.platform} gradle property.
     */
    public final PlatformType getPlatform()
    {
        return platform;
    }

    /**
     * Return {@code true} if the plugin is currently depending on Fabric platform.
     * Otherwise, return {@code false}.
     */
    public final boolean isFabric()
    {
        return platform == PlatformType.FABRIC;
    }

    /**
     * Return {@code true} if the plugin is currently depending on NeoForge platform.
     * Otherwise, return {@code false}.
     */
    public final boolean isNeoForge()
    {
        return platform == PlatformType.NEOFORGE;
    }

    /**
     * Return {@code true} if current environment have Minecraft obfuscated.
     * <p>
     * This property is determined by whether the plugin depends on Fabric platform and the Minecraft
     * version project depends on is below 26.1.
     */
    // TODO: Check if Minecraft is below 26.1.
    public final boolean isObfuscated()
    {
        return isFabric();
    }
}
