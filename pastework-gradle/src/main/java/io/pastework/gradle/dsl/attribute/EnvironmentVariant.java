package io.pastework.gradle.dsl.attribute;

import org.gradle.api.Named;
import org.gradle.api.attributes.Attribute;

public interface EnvironmentVariant extends Named
{
    Attribute<EnvironmentVariant> ENVIRONMENT_VARIANT_ATTRIBUTE = Attribute.of(
        "io.pastework.variant.environment",
        EnvironmentVariant.class
    );

    String COMMON = "common";
    String CLIENT = "client";
}
