plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://maven.fabricmc.net/") }
}

dependencies {
    "implementation"(libs.loom)
    "implementation"(libs.moddev)
    "implementation"(libs.pastework.gradle)
}
