package io.pastework.gradle.dsl.attribute;

import org.gradle.api.Named;
import org.gradle.api.attributes.Attribute;

public interface NamedRuntimeVariant extends Named
{
    Attribute<Boolean> NAMED_RUNTIME_VARIANT_ATTRIBUTE = Attribute.of(
        "io.pastework.variant.named-runtime",
        Boolean.class
    );
}
