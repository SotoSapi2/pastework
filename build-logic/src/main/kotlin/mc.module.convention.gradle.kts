@file:Suppress("UnstableApiUsage")

import io.pastework.gradle.dsl.PasteworkExtension
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.neoforged.moddevgradle.dsl.NeoForgeExtension

plugins {
    id("pastework.gradle") apply false
}

val pastework = extensions.getByType(PasteworkExtension::class.java)

if(pastework.isFabric) {
    val loom = extensions.getByType(LoomGradleExtensionAPI::class.java)
    val mappingLayer = loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
    }

    dependencies {
        "minecraft"(libs.minecraft);
        "mappings"(mappingLayer);
    }
}

if(pastework.isNeoForge) {
    val neoForge = extensions.getByType(NeoForgeExtension::class.java)
    neoForge.version = libs.versions.neoforge.get()
}

dependencies {
    "compileOnly"(libs.mixin)
    "compileOnly"(libs.asm.commons)
    "compileOnly"(libs.mixin.extra)
    "annotationProcessor"(libs.mixin.extra)
}