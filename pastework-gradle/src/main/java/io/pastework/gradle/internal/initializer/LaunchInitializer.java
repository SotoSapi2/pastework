package io.pastework.gradle.internal.initializer;

import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.neoforged.moddevgradle.dsl.ModDevExtension;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.TaskContainer;

import javax.inject.Inject;

public class LaunchInitializer extends AbstractInitializer
{
    @Inject
    public LaunchInitializer(
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
        getProject().afterEvaluate(project ->
        {
            var pasteExt = getPasteworkExtension();

            if(pasteExt.getShouldGenerateRunConfig().get())
            {
                String projectPath = project.getPath();
                String clientConfigName = generateRunConfigName("client");
                String serverConfigName = generateRunConfigName("server");

                if(pasteExt.isFabric())
                {
                    var loomExt = getExtensions().getByType(LoomGradleExtensionAPI.class);
                    var runs = loomExt.getRuns();
                    runs.create(clientConfigName, it ->
                    {
                        it.client();
                        it.setConfigName("Fabric Client");
                        it.ideConfigGenerated(true);
                    });
                    runs.create(serverConfigName, it ->
                    {
                        it.server();
                        it.setConfigName("Fabric Server");
                        it.ideConfigGenerated(true);
                    });
                }

                if(pasteExt.isNeoForge())
                {
                    var moddevExt = getExtensions().getByType(ModDevExtension.class);
                    var runs = moddevExt.getRuns();
                    runs.create(clientConfigName, runModel ->
                    {
                        runModel.getIdeName().set(String.format("NeoForge Client (%s)", projectPath));
                        runModel.client();
                    });
                    runs.create(serverConfigName, runModel ->
                    {
                        runModel.getIdeName().set(String.format("NeoForge Server (%s)", projectPath));
                        runModel.server();
                    });
                }
            }
        });
    }

    private String generateRunConfigName(String side)
    {
        String projectPath = getProject().getPath();
        projectPath = projectPath.replaceFirst(":", "");
        projectPath = projectPath.replace(":", "-");

        return String.format("%s-%s", side, projectPath);
    }
}
