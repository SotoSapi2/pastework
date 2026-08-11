package io.pastework.gradle.internal.initializer;

import io.pastework.gradle.dsl.PasteworkExtension;
import lombok.AccessLevel;
import lombok.Getter;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.logging.Logger;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskContainer;

public abstract class AbstractInitializer implements Runnable
{
    @Getter(AccessLevel.PROTECTED)
    private final Project project;

    @Getter(AccessLevel.PROTECTED)
    private final TaskContainer tasks;

    @Getter(AccessLevel.PROTECTED)
    private final ConfigurationContainer configs;

    @Getter(AccessLevel.PROTECTED)
    private final ObjectFactory objectFactory;

    @Getter(AccessLevel.PROTECTED)
    private final ExtensionContainer extensions;

    @Getter(AccessLevel.PROTECTED)
    private final JavaPluginExtension javaExtension;

    @Getter(AccessLevel.PROTECTED)
    private final PasteworkExtension pasteworkExtension;

    @Getter(AccessLevel.PROTECTED)
    private final Logger logger;

    public AbstractInitializer(
        Project project,
        TaskContainer tasks,
        ConfigurationContainer configs,
        ObjectFactory factory
    )
    {
        this.project = project;
        this.tasks = tasks;
        this.configs = configs;
        this.objectFactory = factory;
        this.extensions = project.getExtensions();
        this.javaExtension = extensions.getByType(JavaPluginExtension.class);
        this.pasteworkExtension = extensions.getByType(PasteworkExtension.class);
        this.logger = project.getLogger();
    }

    protected final SourceSetContainer getSourceSets()
    {
        return javaExtension.getSourceSets();
    }
}
