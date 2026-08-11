plugins {
    alias(libs.plugins.pastework.gradle) apply false
    alias(conventions.plugins.project.convention) apply false
    alias(conventions.plugins.mc.module.convention) apply false
}

val conventionPlugins = conventions.plugins;

fun PluginContainer.applyConvention(provider: Provider<PluginDependency>) {
    apply(provider.get().pluginId)
}

subprojects.forEach { child ->
    // Don't apply conventions plugin outside beforeEvaluate scope.
    // There's a bug regarding typesafe-conventions plugin where LibraryForLibs
    // doesn't exist when you apply them on subprojects iterator scope.
    child.beforeEvaluate {
        child.plugins.applyConvention(libs.plugins.pastework.gradle)
        child.plugins.applyConvention(conventionPlugins.project.convention)
        child.plugins.applyConvention(conventionPlugins.mc.module.convention)
    }
}