version = "1.0.0"

pastework {
    metadata {
        modId = "pastework_demo"
        modVersion = project.version.toString()
        displayName = "Pastework Demo"
        license = "MIT"
        authors = listOf("soto_sapi1")

        mixins {
            register("common.mixins.json")
        }
    }

    shouldGenerateRunConfig = true
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    "modImplementation"(libs.fabric.api)
    "modImplementation"(libs.fabric.loader)

    "compileOnlyApi"(project(":core:api"))
    "localRuntime"(project(":core:fabric"))
}