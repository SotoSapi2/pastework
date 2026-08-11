plugins {
    alias(libs.plugins.pastework.gradle) apply false
    alias(conventions.plugins.project.convention) apply false
    alias(conventions.plugins.mc.module.convention) apply false
}

val modulePublishGroup: String = "io.github.sotoSapi2";
val moduleIDPrefix: String = "pastework"
val moduleMcVersion: String = "1.21.11"
val moduleVersion: String = "1.0.0"
val parentProjectName: String = name;
val conventionPlugins = conventions.plugins;

fun PluginContainer.applyConvention(provider: Provider<PluginDependency>) {
    apply(provider.get().pluginId)
}

subprojects.forEach { child ->
    child.version = moduleVersion;

    val publishArtifactId = "${moduleIDPrefix}-${parentProjectName}-${child.name}-${moduleMcVersion}";

    child.configure<BasePluginExtension> {
        archivesName = publishArtifactId
    }

    // Don't apply conventions plugin outside beforeEvaluate scope.
    // There's a bug regarding typesafe-conventions plugin where LibraryForLibs
    // doesn't exist when you apply them on subprojects iterator scope.
    child.beforeEvaluate {
        child.plugins.applyConvention(libs.plugins.pastework.gradle)
        child.plugins.applyConvention(conventionPlugins.project.convention)
        child.plugins.applyConvention(conventionPlugins.mc.module.convention)

        child.configure<PublishingExtension> {
            publications {
                val publish = create<MavenPublication>("mavenJava")

                publish.groupId = modulePublishGroup;
                publish.artifactId = publishArtifactId
                publish.version = moduleVersion
                publish.from(child.components["java"])
            }
        }
    }
}