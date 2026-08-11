package io.pastework.core.api;

import io.pastework.core.api.common.platform.LoaderType;
import io.pastework.core.api.common.platform.SideType;
import io.pastework.core.api.spi.IRuntimeInformation;
import io.pastework.spi.IPasteworkService;
import io.pastework.spi.IServiceHolder;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;
import java.util.Collection;
import java.util.ServiceLoader;

/**
 * The service provider interface for the framework.
 * <p>
 * This interface provides access to implemented services, such as, registry, networking, runtime information, etc.
 */
@ApiStatus.NonExtendable
public interface Pastework extends IServiceHolder
{
    /**
     * The singleton instance of the interface.
     */
    Pastework INSTANCE = ServiceLoader.load(Pastework.class)
        .findFirst()
        .orElseThrow(() -> new NullPointerException("Couldn't resolve Pastework implementation."));

    /**
     * Gets an instance for every service that has been registered.
     *
     * @return A {@link Collection} of registered services.
     * @throws IllegalStateException if services registration hasn't been finalized.
     */
    Collection<IPasteworkService> getServices();

    /**
     * Checks if the Pastework library has been fully initialized.
     *
     * @return {@code true} if initialized, {@code false} otherwise.
     */
    boolean isInitialized();

    /**
     * Gets the current platform runtime information.
     *
     * @return The {@link IRuntimeInformation} object containing platform details.
     */
    IRuntimeInformation getRuntimeInformation();

    /**
     * Checks if the current environment is the physical client.
     *
     * @return {@code true} if running on the client side, {@code false} otherwise.
     */
    static boolean isClient()
    {
        return INSTANCE.getRuntimeInformation().getEnvironmentType() == SideType.CLIENT;
    }

    /**
     * Checks if the current environment is the physical dedicated server.
     *
     * @return {@code true} if running on the server side, {@code false} otherwise.
     */
    static boolean isServer()
    {
        return INSTANCE.getRuntimeInformation().getEnvironmentType() == SideType.SERVER;
    }

    /**
     * Checks if the current environment is a development environment.
     *
     * @return {@code true} if running in a development environment, {@code false} otherwise.
     */
    static boolean isDevelopmentEnvironment()
    {
        return INSTANCE.getRuntimeInformation().isDevelopmentEnvironment();
    }

    /**
     * Gets the type of mod loader currently in use.
     *
     * @return The current {@link LoaderType}.
     */
    static LoaderType getLoaderType()
    {
        return INSTANCE.getRuntimeInformation().getModLoader();
    }

    /**
     * Gets the root game working directory.
     *
     * @return {@link Path} to the working directory.
     */
    static Path getWorkingDirectory()
    {
        return INSTANCE.getRuntimeInformation().getWorkingDirectory();
    }

    /**
     * Gets the directory used to store configuration files.
     *
     * @return {@link Path} to the config directory.
     */
    static Path getConfigDirectory()
    {
        return INSTANCE.getRuntimeInformation().getConfigDirectory();
    }
}
