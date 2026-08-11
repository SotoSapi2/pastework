package io.pastework.core.api.spi;

import io.pastework.core.api.common.platform.LoaderType;
import io.pastework.core.api.common.platform.SideType;

import java.nio.file.Path;

/**
 * Interface providing runtime information about the game and the loader,
 * such as, which loader is currently running, the game version, information about the physical environment, etc.
 */
public interface IRuntimeInformation
{
    LoaderType getModLoader();

    SideType getEnvironmentType();

    String getGameVersion();

    Path getWorkingDirectory();

    Path getConfigDirectory();

    boolean isDevelopmentEnvironment();
}
