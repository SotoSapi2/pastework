plugins {
    id("project.convention")
}

version = "1.0.0"
group = "io.github.sotoSapi2"

val baseExt = extensions.getByType<BasePluginExtension>()
baseExt.archivesName = "pastework-spi"

configure<PublishingExtension> {
    publications {
        val publish = create<MavenPublication>("mavenJava")

        publish.groupId = group.toString()
        publish.artifactId = baseExt.archivesName.get()
        publish.version = version.toString()
        publish.from(components["java"])
    }
}