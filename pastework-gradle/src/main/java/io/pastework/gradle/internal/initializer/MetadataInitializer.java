package io.pastework.gradle.internal.initializer;

import io.pastework.gradle.dsl.PasteworkConstants;
import io.pastework.gradle.dsl.serializable.MetadataSettings;
import io.pastework.gradle.dsl.PasteworkExtension;
import io.pastework.gradle.dsl.PlatformType;
import io.pastework.gradle.internal.task.FabricMetadataTask;
import io.pastework.gradle.internal.task.NeoMetadataTask;
import lombok.Getter;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.internal.tasks.JvmConstants;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.language.jvm.tasks.ProcessResources;

import javax.inject.Inject;

// TODO: Explicitly throw an error if the required property is not present.
public class MetadataInitializer extends AbstractInitializer
{
    public static final String FABRIC_FILE_NAME = "fabric.mod.json";
    public static final String NEOFORGE_FILE_NAME = "META-INF/neoforge.mods.toml";
    private static final String GENERATE_FABRIC_METADATA_TASK = "generateFabricMetadata";
    private static final String GENERATE_NEOFORGE_METADATA_TASK = "generateNeoForgeMetadata";

    @Getter
    private final PasteworkExtension pasteworkExtension;

    @Inject
    public MetadataInitializer(
        Project project,
        TaskContainer tasks,
        ConfigurationContainer configs,
        ObjectFactory factory
    )
    {
        super(project, tasks, configs, factory);
        this.pasteworkExtension = project.getExtensions()
            .getByType(PasteworkExtension.class);
    }

    @Override
    public void run()
    {
        var outputDir = getProject()
            .getLayout()
            .getBuildDirectory()
            .dir("pastework/metadata")
            .get();

        var fabricMetadata = getTasks().register(
            GENERATE_FABRIC_METADATA_TASK,
            FabricMetadataTask.class,
            task ->
            {
                task.onlyIf(_ -> canGenerateMetadata(PlatformType.FABRIC));
                task.getOutput().set(outputDir.file(FABRIC_FILE_NAME));
            }
        );

        var neoForgeMetadata = getTasks().register(
            GENERATE_NEOFORGE_METADATA_TASK,
            NeoMetadataTask.class,
            task ->
            {
                task.onlyIf(_ -> canGenerateMetadata(PlatformType.NEOFORGE));
                task.getOutput().set(outputDir.file(NEOFORGE_FILE_NAME));
            }
        );

        var metadata = getTasks().register(
            PasteworkConstants.Task.GENERATE_METADATA,
            task ->
            {
                task.setGroup("build");
                task.dependsOn(fabricMetadata.get());
                task.dependsOn(neoForgeMetadata);

                task.getOutputs().dir(outputDir);
            });

        getTasks().named(
            JvmConstants.PROCESS_RESOURCES_TASK_NAME,
            ProcessResources.class,
            task -> task.from(metadata)
                .include("**/*")
        );
    }

    public final boolean isRequiredPropertiesExist()
    {
        var metadataSettingsProperty = pasteworkExtension.getMetadata();

        if(!metadataSettingsProperty.isPresent())
        {
            return false;
        }

        var metadataSettings = metadataSettingsProperty.get();

        return metadataSettings.getModId().isPresent() &&
            metadataSettings.getDisplayName().isPresent() &&
            metadataSettings.getModVersion().isPresent();
    }

    protected final boolean canGenerateMetadata(PlatformType platformType)
    {
        var metadataSettingsProperty = pasteworkExtension.getMetadata();

        if(!metadataSettingsProperty.isPresent())
        {
            return false;
        }

        var metadataSettings = metadataSettingsProperty.get();

        return metadataSettings.getShouldGenerate().get() &&
            isRequiredPropertiesExist() &&
            metadataSettings.getTargetPlatforms().get().contains(platformType);
    }
}
