pastework {
    metadata {
        modId = "pastework_core_neoforge"
        modVersion = project.version.toString()
        displayName = "Pastework Core NeoForge"
        description = "A mod to help other mod developers with cross-platform development. This mod doesn't do anything by itself."
        license = "MIT"
        authors = listOf("soto_sapi1")
        mixins {
            register("neoforge.common.mixins.json")
        }
    }

    shouldGenerateRunConfig = true
}

dependencies {
    "bundle"(project(":core:api"))
    "api"(project(":core:base"))
}
