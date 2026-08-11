plugins {
    java apply false
    `maven-publish` apply false
}

dependencies {
    "compileOnly"(libs.lombok)
    "annotationProcessor"(libs.lombok)
}

tasks.register("debugIncomingArtifacts") {
    val variantProp = project.property("variant") as String
    val configs = configurations.named(variantProp).get();

    println("\n=== Resolved Variant Details ===")

    configs.incoming.artifacts.forEach { artifact ->
        println("Component    : ${artifact.id.componentIdentifier}")
        println("Variant Name : ${artifact.variant.displayName}")
        println("Attributes   : ${artifact.variant.attributes}")
        println("File Name    : ${artifact.file.name}")
        println("Path         : ${artifact.file.absolutePath}")
        println("--------------------------------------------------")
    }
}
