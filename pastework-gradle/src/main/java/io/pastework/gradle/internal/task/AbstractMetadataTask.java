package io.pastework.gradle.internal.task;

import io.pastework.gradle.dsl.PlatformType;
import io.pastework.gradle.dsl.serializable.MetadataSettings;
import io.pastework.gradle.dsl.PasteworkExtension;
import io.pastework.gradle.dsl.serializable.MixinMetadata;
import lombok.AccessLevel;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputFile;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public abstract class AbstractMetadataTask extends DefaultTask
{
    @OutputFile
    public abstract RegularFileProperty getOutput();

    @Nested
    @Getter(AccessLevel.PROTECTED)
    private final MetadataSettings metadataSettings;

    public AbstractMetadataTask(Project project)
    {
        this.metadataSettings = project.getExtensions()
            .getByType(PasteworkExtension.class)
            .getMetadata()
            .get();
    }

    protected final Collection<MixinMetadata> gatherEveryMixins()
    {
        return metadataSettings.getMixins();
    }

    protected final Collection<MixinMetadata> gatherApplicableMixins(PlatformType type)
    {
        return gatherEveryMixins()
            .stream()
            .filter(data -> data.getTargetPlatform().get().contains(type))
            .toList();
    }

    protected final FileWriter createOrOpenFile() throws IOException
    {
        var file = getOutput().getAsFile().get();
        var parentFile = file.getParentFile();

        if (parentFile != null && parentFile.exists())
        {
            parentFile.mkdirs();
        }

        if(!file.exists())
        {
            file.createNewFile();
        }

        return new FileWriter(file);
    }
}
