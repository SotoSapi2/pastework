package io.pastework.gradle.dsl;

import io.pastework.gradle.internal.initializer.*;
import net.fabricmc.loom.LoomGradlePlugin;
import net.neoforged.moddevgradle.boot.ModDevPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.JavaPlugin;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Plugin that handle provides task management such as, generating metadata/mod files, registering mixins, etc.
 * The plugin also manages dependency resolution for both client and common environment source sets.
 * <p>
 * Applies {@link JavaPlugin} and mod platform plugin like
 * {@link LoomGradlePlugin} or {@link ModDevPlugin} depending on
 * {@code pastework.platform_dependency} gradle property.
 */
public class PasteworkPlugin implements Plugin<Project>
{
    @Override
    public void apply(Project target)
    {
        if (target.getName().equals("gradle-kotlin-dsl-accessors"))
        {
            return;
        }

        var plugins = target.getPlugins();
        var logger = target.getLogger();
        @Nullable var platform = target.findProperty(PasteworkConstants.Property.PLATFORM_DEPENDENCY);

        target.getPlugins().apply(JavaPlugin.class);

        if(platform instanceof String platformStr)
        {
            PlatformType platformType = PlatformType.parse(platformStr);

            switch (platformType)
            {
                case FABRIC ->
                {
                    plugins.apply(LoomGradlePlugin.class);
                    createPasteworkExtension(target, PlatformType.FABRIC);
                }
                case NEOFORGE ->
                {
                    plugins.apply(ModDevPlugin.class);
                    createPasteworkExtension(target, PlatformType.NEOFORGE);
                }
            }
        }
        else
        {
            logger.lifecycle("Platform property is unspecified. Using Fabric by default.");
            plugins.apply(LoomGradlePlugin.class);
            createPasteworkExtension(target, PlatformType.FABRIC);
        }

        executeConfigs(target);
    }

    private void executeConfigs(Project target)
    {
        var configs = createPlatformConfigs(target.getObjects());

        for(var config : configs)
        {
            config.run();
        }
    }

    private void createPasteworkExtension(Project target, PlatformType platform)
    {
        var extensions = target.getExtensions();
        extensions.create(
            PasteworkConstants.PLATFORM_EXTENSION_NAME,
            PasteworkExtension.class,
            platform,
            target.getObjects()
        );
    }

    private Collection<AbstractInitializer> createPlatformConfigs(ObjectFactory objectFactory)
    {
        return List.of(
            objectFactory.newInstance(PlatformInitializer.class),
            objectFactory.newInstance(ConfigurationsInitializer.class),
            objectFactory.newInstance(LaunchInitializer.class),
            objectFactory.newInstance(MetadataInitializer.class)
        );
    }
}
