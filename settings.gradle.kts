pluginManagement {
    repositories {
        maven { url = uri("https://maven.fabricmc.net/") }
        maven { url = uri("https://files.minecraftforge.net/maven/") }
        maven { url = uri("https://maven.kikugie.dev/snapshots") }
        mavenLocal()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

rootProject.name = "pastework"

includeBuild("build-logic")
includeBuild("pastework-gradle")
include(":spi")

include(":core")
include(":core:api")
include(":core:base")
include(":core:fabric")
include(":core:neoforge")

include("demo-mod:main-module")
include("demo-mod:neoforge-runtime")