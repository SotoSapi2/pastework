package io.pastework.gradle.internal.initializer;

import io.pastework.gradle.dsl.PasteworkConstants;
import io.pastework.gradle.dsl.PasteworkExtension;
import io.pastework.gradle.dsl.attribute.EnvironmentVariant;
import io.pastework.gradle.dsl.attribute.NamedRuntimeVariant;
import io.pastework.gradle.dsl.attribute.ProductionPlatformVariant;
import net.fabricmc.loom.configuration.providers.minecraft.MinecraftSourceSets;
import net.fabricmc.loom.task.RemapTaskConfiguration;
import net.fabricmc.loom.util.Constants;
import net.neoforged.moddevgradle.tasks.JarJar;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.attributes.*;
import org.gradle.api.attributes.java.TargetJvmVersion;
import org.gradle.api.component.AdhocComponentWithVariants;
import org.gradle.api.component.ConfigurationVariantDetails;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DuplicatesStrategy;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.jvm.tasks.Jar;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

public abstract class ConfigurationsInitializer extends AbstractInitializer
{
    private static final String MODDEV_MAIN_JAR_JAR_CONFIG = "jarJar";
    private static final String CLIENT_RUNTIME_CLASSPATH = "clientRuntimeClasspath";

    @Inject
    public ConfigurationsInitializer(
        Project project,
        TaskContainer tasks,
        ConfigurationContainer configs,
        ObjectFactory factory
    )
    {
        super(project, tasks, configs, factory);
    }

    @Override
    public void run()
    {
        SourceSetContainer sourceSets = getSourceSets();
        PasteworkExtension pasteworkExt = getPasteworkExtension();

        var devArtifactsDirectory = getDevArtifactsDirectory();
        var mainSource = sourceSets.named(SourceSet.MAIN_SOURCE_SET_NAME).get();
        registersEnvironmentJarTask(
            PasteworkConstants.Task.COMMON_ENV_JAR,
            mainSource,
            EnvironmentVariant.COMMON,
            devArtifactsDirectory
        );

        if (pasteworkExt.isFabric())
        {
            var clientSource = sourceSets.named(MinecraftSourceSets.Split.CLIENT_ONLY_SOURCE_SET_NAME).get();
            registersMergedJarTask(
                PasteworkConstants.Task.CLIENT_ENV_JAR,
                EnvironmentVariant.CLIENT,
                List.of(mainSource, clientSource),
                devArtifactsDirectory
            );

            setupNeoForgeProductionJarTasks();
            setupProductionConfiguration();
        }

        setupEnvironmentVariants();
        setupDevelopmentRuntimeConfiguration();
        setupBundleConfiguration();
    }

    private void setupNeoForgeProductionJarTasks()
    {
        SourceSetContainer sourceSets = getSourceSets();
        var mainSource = sourceSets.named(SourceSet.MAIN_SOURCE_SET_NAME).get();
        var clientSource = sourceSets.named(MinecraftSourceSets.Split.CLIENT_ONLY_SOURCE_SET_NAME).get();

        var jarJarTask = JarJar.registerWithConfiguration(
            getProject(),
            MODDEV_MAIN_JAR_JAR_CONFIG
        ).get();

        var standardOutputDirectory = getStandardArtifactsDirectory();
        var productionJar = registersMergedJarTask(
            PasteworkConstants.Task.NEOFORGE_PRODUCTION_JAR,
            "neoforge",
            List.of(mainSource, clientSource),
            standardOutputDirectory
        );

        productionJar.setGroup("build");
        productionJar.dependsOn(jarJarTask);
        productionJar.from(jarJarTask);

        getTasks().named(BasePlugin.ASSEMBLE_TASK_NAME, task ->
        {
           task.dependsOn(productionJar);
        });
    }

    private void setupBundleConfiguration()
    {
        PasteworkExtension pasteworkExt = getPasteworkExtension();
        registerNonTransitive(PasteworkConstants.Configuration.BUNDLE, Role.NONE);

        if (pasteworkExt.isFabric())
        {
            extendsFrom(Constants.Configurations.INCLUDE, PasteworkConstants.Configuration.BUNDLE);
            getConfigs().named(Constants.Configurations.INCLUDE_INTERNAL).configure(config ->
            {
                var attributes = config.getAttributes();
                attributes.attribute(
                    ProductionPlatformVariant.PRODUCTION_PLATFORM_VARIANT_ATTRIBUTE,
                    getObjectFactory().named(ProductionPlatformVariant.class, ProductionPlatformVariant.FABRIC)
                );
            });

        }

        assert getConfigs().findByName(MODDEV_MAIN_JAR_JAR_CONFIG) != null :
            String.format("%s is not present. This is a bug!", MODDEV_MAIN_JAR_JAR_CONFIG);

        getConfigs().named(MODDEV_MAIN_JAR_JAR_CONFIG).configure(config ->
        {
            var attributes = config.getAttributes();
            attributes.attribute(
                ProductionPlatformVariant.PRODUCTION_PLATFORM_VARIANT_ATTRIBUTE,
                getObjectFactory().named(ProductionPlatformVariant.class, ProductionPlatformVariant.NEOFORGE)
            );
        });

        extendsFrom(MODDEV_MAIN_JAR_JAR_CONFIG, PasteworkConstants.Configuration.BUNDLE);
    }

    private void setupEnvironmentVariants()
    {
        PasteworkExtension pasteworkExt = getPasteworkExtension();
        SourceSetContainer sourceSets = getSourceSets();

        var mainSource = sourceSets.named(SourceSet.MAIN_SOURCE_SET_NAME).get();

        if (pasteworkExt.isFabric())
        {
            var clientSource = sourceSets.named(MinecraftSourceSets.Split.CLIENT_ONLY_SOURCE_SET_NAME).get();
            tagSourceSetEnvironment(mainSource, EnvironmentVariant.COMMON);
            tagSourceSetEnvironment(clientSource, EnvironmentVariant.CLIENT);

            var commonElements = register(PasteworkConstants.Configuration.COMMON_API_ELEMENTS, Role.CONSUMABLE);
            setupPublishVariant(commonElements);
            setupConfigUsageAttributes(commonElements, Usage.JAVA_API);
            commonElements.getAttributes().attribute(
                EnvironmentVariant.ENVIRONMENT_VARIANT_ATTRIBUTE,
                getObjectFactory().named(EnvironmentVariant.class, EnvironmentVariant.COMMON)
            );

            extendsFrom(PasteworkConstants.Configuration.COMMON_API_ELEMENTS, JavaPlugin.API_CONFIGURATION_NAME);

            var commonJarTask = getTasks().named(PasteworkConstants.Task.COMMON_ENV_JAR).get();
            commonElements.getOutgoing().artifact(commonJarTask);

            var clientElements = register(PasteworkConstants.Configuration.CLIENT_API_ELEMENTS, Role.CONSUMABLE);
            setupPublishVariant(clientElements);
            setupConfigUsageAttributes(clientElements, Usage.JAVA_API);
            clientElements.getAttributes().attribute(
                EnvironmentVariant.ENVIRONMENT_VARIANT_ATTRIBUTE,
                getObjectFactory().named(EnvironmentVariant.class, EnvironmentVariant.CLIENT)
            );

            extendsFrom(PasteworkConstants.Configuration.CLIENT_API_ELEMENTS, JavaPlugin.API_CONFIGURATION_NAME);

            var clientJarTask = getTasks().named(PasteworkConstants.Task.CLIENT_ENV_JAR).get();
            clientElements.getOutgoing().artifact(clientJarTask);
        }

        // At the moment, ModDev plugin doesn't support source splitting, and they merge both client and common/server
        // source set into one. So we can tell the main source set to use client artifact since it contains
        // both server and client classes.
        if (pasteworkExt.isNeoForge())
        {
            tagSourceSetEnvironment(mainSource, EnvironmentVariant.CLIENT);

            var clientElements = register(PasteworkConstants.Configuration.CLIENT_API_ELEMENTS, Role.CONSUMABLE);
            setupConfigUsageAttributes(clientElements, Usage.JAVA_API);
            clientElements.getAttributes().attribute(
                EnvironmentVariant.ENVIRONMENT_VARIANT_ATTRIBUTE,
                getObjectFactory().named(EnvironmentVariant.class, EnvironmentVariant.CLIENT)
            );

            extendsFrom(PasteworkConstants.Configuration.CLIENT_API_ELEMENTS, JavaPlugin.API_CONFIGURATION_NAME);

            var clientJarTask = getTasks().named(PasteworkConstants.Task.COMMON_ENV_JAR).get();
            clientElements.getOutgoing().artifact(clientJarTask);
        }
    }

    // Loom generate obfuscated artifact to runtimeElements. But we don't need it.
    // We can just use unobfuscated artifact generated from namedElements
    // as runtime provider for development environment.
    //
    // For ModDev to ModDev dependency, consumers can use runtimeElements because NeoForge
    // runtime is not obfuscated.
    private void setupDevelopmentRuntimeConfiguration()
    {
        PasteworkExtension pasteworkExt = getPasteworkExtension();

        var runtimeClasspath = getConfigs().named(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME).get();
        runtimeClasspath.getAttributes().attribute(
            NamedRuntimeVariant.NAMED_RUNTIME_VARIANT_ATTRIBUTE,
            true
        );

        if (pasteworkExt.isFabric() && pasteworkExt.isObfuscated())
        {
            var namedRuntimeElements = register(
                PasteworkConstants.Configuration.NAMED_RUNTIME_ELEMENTS,
                Role.CONSUMABLE
            );

            setupConfigUsageAttributes(namedRuntimeElements, Usage.JAVA_RUNTIME);
            namedRuntimeElements.getAttributes().attribute(
                NamedRuntimeVariant.NAMED_RUNTIME_VARIANT_ATTRIBUTE,
                true
            );

            setupPublishVariant(namedRuntimeElements);
            var clientRuntimeClasspath = getConfigs().named(CLIENT_RUNTIME_CLASSPATH).get();
            clientRuntimeClasspath.getAttributes().attribute(
                NamedRuntimeVariant.NAMED_RUNTIME_VARIANT_ATTRIBUTE,
                true
            );

            // Let namedRuntimeElements owns unobfuscated artifact from namedElements provided by Loom.
            var namedElements = getConfigs().named(Constants.Configurations.NAMED_ELEMENTS).get();
            namedRuntimeElements.extendsFrom(namedElements);
        }
    }

    private void setupProductionConfiguration()
    {
        var neoforgeArtifact = getTasks().named(PasteworkConstants.Task.NEOFORGE_PRODUCTION_JAR);
        var fabricArtifact = getTasks().named(RemapTaskConfiguration.REMAP_JAR_TASK_NAME);

        var fabricElements = register(
            PasteworkConstants.Configuration.FABRIC_PRODUCTION_ELEMENTS,
            Role.CONSUMABLE
        );

        setupConfigUsageAttributes(fabricElements, Usage.JAVA_RUNTIME);
        fabricElements.getOutgoing().artifact(fabricArtifact);
        fabricElements.getAttributes().attribute(
            ProductionPlatformVariant.PRODUCTION_PLATFORM_VARIANT_ATTRIBUTE,
            getObjectFactory().named(ProductionPlatformVariant.class, ProductionPlatformVariant.FABRIC)
        );
        setupPublishVariant(fabricElements);

        var neoforgeElements = register(
            PasteworkConstants.Configuration.NEOFORGE_PRODUCTION_ELEMENTS,
            Role.CONSUMABLE
        );

        setupConfigUsageAttributes(neoforgeElements, Usage.JAVA_RUNTIME);
        neoforgeElements.getOutgoing().artifact(neoforgeArtifact);
        neoforgeElements.getAttributes().attribute(
            ProductionPlatformVariant.PRODUCTION_PLATFORM_VARIANT_ATTRIBUTE,
            getObjectFactory().named(ProductionPlatformVariant.class, ProductionPlatformVariant.NEOFORGE)
        );
        setupPublishVariant(neoforgeElements);
    }

    private void registersEnvironmentJarTask(
        String taskName,
        SourceSet sourceSet,
        String classifier,
        Directory outputDirectory
    )
    {
        getTasks().register(taskName, Jar.class, task ->
        {
            task.getDestinationDirectory().convention(outputDirectory);
            task.getArchiveClassifier().set(classifier);
            task.from(sourceSet.getOutput());
            task.setDuplicatesStrategy(DuplicatesStrategy.INCLUDE);
        }).get();
    }

    private Jar registersMergedJarTask(
        String taskName,
        String classifier,
        Collection<SourceSet> sourceSets,
        Directory outputDirectory
    )
    {
        return getTasks().register(
            taskName, Jar.class, task ->
            {
                task.getDestinationDirectory().convention(outputDirectory);
                task.getArchiveClassifier().set(classifier);

                for (var sourceSet : sourceSets)
                {
                    task.from(sourceSet.getOutput());
                }
            }
        ).get();
    }

    private Directory getStandardArtifactsDirectory()
    {
        return getProject()
            .getLayout()
            .getBuildDirectory()
            .dir("libs")
            .get();
    }

    private Directory getDevArtifactsDirectory()
    {
        return getProject()
            .getLayout()
            .getBuildDirectory()
            .dir("pastework/devArtifacts")
            .get();
    }

    private Configuration register(String name, Role role)
    {
        return getConfigs().register(name, role::apply).get();
    }

    private Configuration registerNonTransitive(String name, Role role)
    {
        final Configuration config = register(name, role);
        config.setTransitive(false);

        return config;
    }

    private void extendsFrom(String forConfig, String extend)
    {
        getConfigs().named(
            forConfig, configuration ->
            {
                final var extendConfig = getConfigs().named(extend).get();
                configuration.extendsFrom(extendConfig);
            }
        );
    }

    private void tagSourceSetEnvironment(SourceSet sourceSet, String environmentVariant)
    {
        String compileClasspath = sourceSet.getCompileClasspathConfigurationName();
        Configuration config = getConfigs().named(compileClasspath).get();
        AttributeContainer attributes = config.getAttributes();

        attributes.attribute(
            EnvironmentVariant.ENVIRONMENT_VARIANT_ATTRIBUTE,
            getObjectFactory().named(EnvironmentVariant.class, environmentVariant)
        );
    }

    private void setupConfigUsageAttributes(Configuration config, String usage)
    {
        int javaVersion = getJavaTargetVersion();
        AttributeContainer attributes = config.getAttributes();

        attributes.attribute(
            Usage.USAGE_ATTRIBUTE,
            getObjectFactory().named(Usage.class, usage)
        );

        attributes.attribute(
            TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE,
            javaVersion
        );

        attributes.attribute(
            Bundling.BUNDLING_ATTRIBUTE,
            getObjectFactory().named(Bundling.class, Bundling.EXTERNAL)
        );

        attributes.attribute(
            Category.CATEGORY_ATTRIBUTE,
            getObjectFactory().named(Category.class, Category.LIBRARY)
        );

        attributes.attribute(
            LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE,
            getObjectFactory().named(LibraryElements.class, LibraryElements.JAR)
        );
    }

    private void setupPublishVariant(Configuration configuration)
    {
        var javaComponents = (AdhocComponentWithVariants) getProject().getComponents().getByName("java");
        javaComponents.addVariantsFromConfiguration(configuration, ConfigurationVariantDetails::mapToOptional);
    }

    private int getJavaTargetVersion()
    {
        return getJavaExtension().getTargetCompatibility().ordinal() + 1;
    }

    public enum Role
    {
        NONE(false, false),
        CONSUMABLE(true, false),
        RESOLVABLE(false, true);

        private final boolean canBeConsumed;
        private final boolean canBeResolved;

        Role(boolean canBeConsumed, boolean canBeResolved)
        {
            this.canBeConsumed = canBeConsumed;
            this.canBeResolved = canBeResolved;
        }

        public void apply(Configuration configuration)
        {
            configuration.setCanBeConsumed(canBeConsumed);
            configuration.setCanBeResolved(canBeResolved);
        }
    }
}