package io.pastework.gradle.dsl;

/**
 * Enum to represents mod platform
 */
public enum PlatformType
{
    FABRIC,
    NEOFORGE;

    /**
     * Parse {@link PlatformType} enum from a string.
     * <p>
     * The input is not case-insensitive and must be either "fabric" or "neoforge".
     *
     * @param input the input string to parse
     * @throws IllegalStateException if the parsing attempt failed
     */
    public static PlatformType parse(String input) throws IllegalStateException
    {
        return switch (input.toLowerCase())
        {
            case "fabric" -> PlatformType.FABRIC;
            case "neoforge" -> PlatformType.NEOFORGE;
            default ->  throw new IllegalStateException(String.format(
                "Invalid platform type. Passed platform: %s",
                input
            ));
        };
    }
}
