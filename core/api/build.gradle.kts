pastework {
    metadata {
        modId = "pastework_core_api"
        modVersion = project.version.toString()
        displayName = "Pastework Core API"
        description = "A mod to help other mod developers with cross-platform development. This mod doesn't do anything by itself."
        license = "MIT"
        authors = listOf("soto_sapi1")
    }
}

dependencies {
    "api"(project(":spi"))
}