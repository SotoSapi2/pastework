package io.pastework.gradle.dsl.serializable.neoforge;

import io.pastework.gradle.dsl.serializable.fabric.FabricMetadataDependency;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.tasks.Nested;

import java.io.Serializable;

public abstract class MetadataDependencySettings implements Serializable
{
    @Nested
    public abstract NamedDomainObjectContainer<NeoMetadataDependency> getNeoForge();

    @Nested
    public abstract NamedDomainObjectContainer<FabricMetadataDependency> getFabric();
}
