package io.pastework.gradle.internal.initializer;

import io.pastework.gradle.dsl.PasteworkConstants;
import io.pastework.gradle.dsl.PasteworkExtension;
import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.util.Constants;
import net.neoforged.moddevgradle.dsl.ModDevExtension;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskContainer;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class PlatformInitializer extends AbstractInitializer
{
    @Inject
    public PlatformInitializer(
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
        PasteworkExtension pasteworkExt = getPasteworkExtension();

        if(pasteworkExt.isFabric())
        {
            var loomExt = getExtensions().getByType(LoomGradleExtensionAPI.class);
            loomExt.splitEnvironmentSourceSets();
        }

        if(pasteworkExt.isNeoForge())
        {
            var moddev = getExtensions().getByType(ModDevExtension.class);
            var modModelContainer = moddev.getMods();
            var mainSourceSet = getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);
            var modModel = modModelContainer.register(mainSourceSet.getName()).get();

            modModel.sourceSet(mainSourceSet);
        }
    }
}
