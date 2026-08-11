package io.pastework.gradle.internal.task;

import io.pastework.gradle.dsl.PlatformType;
import io.pastework.gradle.dsl.TargetSide;
import io.pastework.gradle.dsl.serializable.fabric.FabricMetadataDependency;
import io.pastework.gradle.dsl.serializable.MixinMetadata;
import io.pastework.gradle.dsl.serializable.fabric.FabricEntrypointSettings;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;

/**
 * Generates {@code fabric.mod.json} for the Fabric platform.
 */
public abstract class FabricMetadataTask extends AbstractMetadataTask
{
    @Inject
    public FabricMetadataTask(Project project)
    {
        super(project);
    }

    @TaskAction
    public void generate() throws IOException
    {
        final var metadataSettings = getMetadataSettings();
        final var fabricSettings = metadataSettings.getFabric();
        final var json = new JsonObject();
        final var gsonBuilder = new GsonBuilder()
            .setPrettyPrinting()
            .create();

        json.addProperty("schemaVersion", 1);
        json.addProperty("id", metadataSettings.getModId().get());
        json.addProperty("version", metadataSettings.getModVersion().get());
        json.addProperty("name", metadataSettings.getDisplayName().get());
        json.addProperty("environment", createEnvironmentString(fabricSettings.getEnvironment().get()));

        addOptionalProperty(json, "description", metadataSettings.getDescription());
        addOptionalProperty(json, "authors", metadataSettings.getAuthors());
        addOptionalProperty(json, "license", metadataSettings.getLicense());

        var entrypointObj = createEntrypointObject(fabricSettings.getEntrypoints());
        json.add("entrypoints", entrypointObj);

        var mixins = gatherApplicableMixins(PlatformType.FABRIC);
        var mixinsArray = createMixinArray(mixins);
        json.add("mixins", mixinsArray);

        emitDependenciesObject(
            json,
            metadataSettings.getDependencies().getFabric()
        );

        try (final var writer = createOrOpenFile())
        {
            gsonBuilder.toJson(json, writer);
            writer.flush();
        }
    }

    private void addOptionalProperty(JsonObject object, String name, Property<String> property)
    {
        if (property.isPresent())
        {
            object.addProperty(name, property.get());
        }
    }
    
    private void addOptionalProperty(JsonObject object, String name, ListProperty<String> property)
    {
        if (property.isPresent())
        {
            var array = new JsonArray();
            for (var data : property.get())
            {
                array.add(data);
            }

            object.add(name, array);
        }
    }

    private JsonObject createEntrypointObject(Property<FabricEntrypointSettings> property)
    {
        var out = new JsonObject();

        if (!property.isPresent())
        {
            return out;
        }

        var entrypointSettings = property.get();
        addOptionalProperty(out, "main", entrypointSettings.getMainEntrypoints());
        addOptionalProperty(out, "client", entrypointSettings.getClientEntrypoints());
        addOptionalProperty(out, "preLaunch", entrypointSettings.getPreLaunchEntrypoints());

        return out;
    }

    private JsonArray createMixinArray(Collection<MixinMetadata> mixinCollection)
    {
        var mixinsArray = new JsonArray();
        for (var mixin : mixinCollection)
        {
            String targetSide = createEnvironmentString(mixin.getTargetSide().get());

            var mixinObj = new JsonObject();
            mixinObj.addProperty("config", mixin.getName());
            mixinObj.addProperty("environment", targetSide);
            mixinsArray.add(mixinObj);
        }

        return mixinsArray;
    }

    private void emitDependenciesObject(
        JsonObject object,
        NamedDomainObjectContainer<FabricMetadataDependency> dependencies
    )
    {
        var dependsObj = new JsonObject();
        var recommendsObj = new JsonObject();
        var suggestsObj = new JsonObject();
        var conflictsObj = new JsonObject();
        var breaksObj = new JsonObject();

        for (var dependency : dependencies)
        {
            var type = dependency.getType()
                .getOrElse(FabricMetadataDependency.Type.DEPENDS);

            JsonObject targetObj = switch (type)
            {
                case DEPENDS -> dependsObj;
                case RECOMMENDS -> recommendsObj;
                case SUGGESTS -> suggestsObj;
                case CONFLICTS -> conflictsObj;
                case BREAKS -> breaksObj;
            };

            targetObj.addProperty(dependency.getName(), dependency.getVersion().get());
        }

        object.add("depends", dependsObj);
        object.add("recommends", recommendsObj);
        object.add("suggests", suggestsObj);
        object.add("conflicts", conflictsObj);
        object.add("breaks", breaksObj);
    }

    private String createEnvironmentString(TargetSide targetSide)
    {
        return switch (targetSide)
        {
            case ANY -> "*";
            case SERVER -> "server";
            case CLIENT -> "client";
        };
    }
}
