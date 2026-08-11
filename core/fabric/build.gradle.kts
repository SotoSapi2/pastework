import io.pastework.gradle.dsl.TargetSide

pastework {
    shouldGenerateRunConfig = true

    metadata {
        modId = "pastework_core_fabric"
        modVersion = project.version.toString()
        displayName = "Pastework Core Fabric"
        description = "A mod to help other mod developers with cross-platform development. This mod doesn't do anything by itself."
        license = "MIT"
        authors = listOf("soto_sapi1")

        fabric {
            entrypoints {
                mainEntrypoints.set(listOf(
                    "io.pastework.core.fabric.common.FabricPasteworkEntrypoint"
                ))
            }
        }

        mixins {
            register("fabric.common.mixins.json")
            register("fabric.client.mixins.json") {
                targetSide = TargetSide.CLIENT
            }
        }
    }
}

dependencies {
    "modImplementation"(libs.fabric.loader)
    "modImplementation"(libs.fabric.api)

    "bundle"(project(":core:base"))
    "api"(project(":core:base"))
}
