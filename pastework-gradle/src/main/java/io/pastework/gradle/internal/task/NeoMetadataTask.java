package io.pastework.gradle.internal.task;

import io.github.wasabithumb.jtoml.JToml;
import io.github.wasabithumb.jtoml.value.array.TomlArray;
import io.github.wasabithumb.jtoml.value.table.TomlTable;
import io.pastework.gradle.dsl.PlatformType;
import io.pastework.gradle.dsl.serializable.MixinMetadata;
import io.pastework.gradle.dsl.serializable.neoforge.NeoMetadataDependency;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;

/**
 * Generates {@code META-INF/neoforge.mods.toml} for the NeoForge platform.
 */
public abstract class NeoMetadataTask extends AbstractMetadataTask
{
    @Inject
    public NeoMetadataTask(Project project)
    {
        super(project);
    }

    @TaskAction
    public void generate() throws IOException
    {
        final var metadataSettings = getMetadataSettings();
        final var jToml = JToml.jToml();
        final var rootTable = TomlTable.create();

        rootTable.put("modLoader", "javafml");
        rootTable.put("loaderVersion", "[1,)");

        if (metadataSettings.getLicense().isPresent())
        {
            rootTable.put("license", metadataSettings.getLicense().get());
        }

        final var modTableArray = TomlArray.create();
        final var modTable = TomlTable.create();
        modTable.put("modId", metadataSettings.getModId().get());
        modTable.put("version", metadataSettings.getModVersion().get());
        modTable.put("displayName", metadataSettings.getDisplayName().get());

        addOptionalProperty(modTable, "description", metadataSettings.getDescription());

        if (metadataSettings.getAuthors().get().isEmpty())
        {
            modTable.put("authors", String.join(", ", metadataSettings.getAuthors().get()));
        }

        modTableArray.add(modTable);
        rootTable.put("mods", modTableArray);

        var mixins = gatherApplicableMixins(PlatformType.NEOFORGE);
        var mixinArray = crateMixinArray(mixins);
        rootTable.put("mixins", mixinArray);

        var depsTable = createDependenciesTable(
            metadataSettings.getDependencies().getNeoForge()
        );
        rootTable.put(
            String.format("dependencies.%s", metadataSettings.getModId().get()),
            depsTable
        );

        try (final var writer = createOrOpenFile())
        {
            jToml.write(writer, rootTable);
            writer.flush();
        }
    }

    private TomlArray crateMixinArray(Collection<MixinMetadata> mixins)
    {
        TomlArray array = TomlArray.create();
        for (var mixin : mixins)
        {
            final var mixinTable = TomlTable.create();
            mixinTable.put("config", mixin.getName());
            array.add(mixinTable);
        }

        return array;
    }

    private void addOptionalProperty(TomlTable table, String name, ListProperty<String> property)
    {
        if(property.isPresent())
        {
            final var array = TomlArray.create();
            for(var data : property.get())
            {
                array.add(data);
            }

            table.put(name, array);
        }
    }

    private void addOptionalProperty(TomlTable table, String name, Property<String> property)
    {
        if(property.isPresent())
        {
            table.put(name, property.get());
        }
    }

    private TomlArray createDependenciesTable(NamedDomainObjectContainer<NeoMetadataDependency> dependencies)
    {
        final var dependencyArray = TomlArray.create();
        for (var dependency : dependencies)
        {
            var depTable = TomlTable.create();
            depTable.put("modId", dependency.getName());
            depTable.put("type", dependency.getType().get().name().toLowerCase());
            depTable.put("versionRange", dependency.getVersion().get());
            depTable.put("ordering", dependency.getOrdering().get().name());

            if (dependency.getSide().isPresent())
            {
                String sideStr = switch (dependency.getSide().get())
                {
                    case ANY -> "BOTH";
                    case CLIENT -> "CLIENT";
                    case SERVER -> "SERVER";
                };
                depTable.put("side", sideStr);
            }

            dependencyArray.add(depTable);
        }

        return dependencyArray;
    }
}
