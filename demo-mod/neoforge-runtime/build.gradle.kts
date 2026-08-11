pastework {
    metadata {
        displayName = "Dummy"
        modId = "dummy"
        modVersion = "1.0.0"
        description = "Dummy"
        license = "MIT"
        authors = listOf("Dummy")
    }

    shouldGenerateRunConfig = true
}

dependencies {
    "runtimeOnly"(project(":demo-mod:main-module"))
    "runtimeOnly"(project(":core:neoforge"))
}