import io.pastework.gradle.dsl.TargetSide

pastework {
    metadata {
        modId = "pastework_core_base"
        modVersion = project.version.toString()
        displayName = "Pastework Core Base"
        description = "A mod to help other mod developers with cross-platform development. This mod doesn't do anything by itself."
        license = "MIT"
        authors = listOf("soto_sapi1")

        mixins {
            register("api.common.mixins.json")
            register("api.client.mixins.json") {
                targetSide = TargetSide.CLIENT
            }
            register("base.client.mixins.json") {
                targetSide = TargetSide.CLIENT
            }
        }
    }
}

dependencies {
    "bundle"(project(":core:api"))
    "api"(project(":core:api"))
}