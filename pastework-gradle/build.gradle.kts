plugins {
    id("java-gradle-plugin")
    id("maven-publish")
}

repositories {
    maven { url = uri("https://maven.fabricmc.net/") }
    gradlePluginPortal()
    mavenCentral()
}

group = "io.github.sotoSapi2"
version = "1.0.0"

dependencies {
    "implementation"(libs.loom)
    "implementation"(libs.moddev)

    "compileOnly"(libs.lombok)
    "annotationProcessor"(libs.lombok)

    "implementation"("com.google.code.gson:gson:2.14.0")
    "implementation"("io.github.wasabithumb:jtoml:1.5.2")
    "implementation"("com.github.javaparser:javaparser-symbol-solver-core:3.28.1")
}

configure<JavaPluginExtension> {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

configure<PublishingExtension> {
    publications {
        val publish = create<MavenPublication>("mavenJava")

        publish.groupId = group.toString()
        publish.artifactId = name
        publish.version = version.toString()
        publish.from(components["java"])
    }
}

gradlePlugin {
    plugins {
        create("pastework") {
            id = "pastework.gradle"
            implementationClass = "io.pastework.gradle.dsl.PasteworkPlugin"
        }
    }
}