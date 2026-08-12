package io.pastework.gradle.dsl.attribute;

import org.gradle.api.Named;
import org.gradle.api.attributes.Attribute;

public interface ProductionPlatformVariant extends Named
{
    Attribute<ProductionPlatformVariant> PRODUCTION_PLATFORM_VARIANT_ATTRIBUTE = Attribute.of(
        "io.pastework.variant.production-platform",
        ProductionPlatformVariant.class
    );

    String FABRIC = "fabric";
    String NEOFORGE = "neoforge";
}
