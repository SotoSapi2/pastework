package io.pastework.gradle.dsl;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PasteworkConstants
{
    @SuppressWarnings("unused")
    public static final String PLUGIN_ID = "pastework.gradle";
    public static final String PLATFORM_EXTENSION_NAME = "pastework";

    @UtilityClass
    public static class Property
    {
        public static final String PLATFORM_DEPENDENCY = "pastework.platform_dependency";
    }

    @UtilityClass
    public static class Configuration
    {
        public static final String BUNDLE = "bundle";
        public static final String COMMON_API_ELEMENTS = "commonApiElements";
        public static final String CLIENT_API_ELEMENTS = "clientApiElements";

        /**
         * Legacy configuration. Used to provide mod dependency runtime in development environment.
         * <p>
         * This configuration exist as obfuscation work around and
         * only created if the current project depends on Fabric platform.
         * From 26.1 MC version and onwards, this configuration won't be created.
         */
        public static final String NAMED_RUNTIME_ELEMENTS = "namedRuntimeElements";

        /**
         * Legacy configuration. Used to provide mod dependency runtime in development environment.
         * <p>
         * This configuration exist as obfuscation work around and
         * only created if the current project depends on Fabric platform.
         * From 26.1 MC version and onwards, this configuration won't be created.
         */
        public static final String FABRIC_PRODUCTION_ELEMENTS = "fabricProductionElements";

        /**
         * Legacy configuration. Used to provide mod dependency runtime in development environment.
         * <p>
         * This configuration exist as obfuscation work around and
         * only created if the current project depends on Fabric platform.
         * From 26.1 MC version and onwards, this configuration won't be created.
         */
        public static final String NEOFORGE_PRODUCTION_ELEMENTS = "neoforgeProductionElements";
    }

    @UtilityClass
    public static class Task
    {
        public static final String GENERATE_METADATA = "generateMetadata";
        public static final String COMMON_ENV_JAR = "commonEnvJar";
        public static final String CLIENT_ENV_JAR = "clientEnvJar";
        public static final String NEOFORGE_PRODUCTION_JAR = "neoForgeProductionJar";
    }

    @UtilityClass
    public static class Repository
    {
        public static final String ParchmentMCName = "ParchmentMC";
        public static final String ParchmentMCUrl = "https://maven.parchmentmc.org";
        public static final String SpongePoweredName = "SpongePowered";
        public static final String SpongePoweredUrl = "https://repo.spongepowered.org/maven/";
        public static final String NeoForgedName = "NeoForged";
        public static final String NeoForgedUrl = "https://maven.neoforged.net/releases/";
    }
}