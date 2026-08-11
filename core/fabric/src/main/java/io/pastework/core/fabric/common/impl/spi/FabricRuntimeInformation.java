package io.pastework.core.fabric.common.impl.spi;

import io.pastework.core.api.common.platform.LoaderType;
import io.pastework.core.api.spi.IRuntimeInformation;
import io.pastework.core.api.common.platform.SideType;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricRuntimeInformation implements IRuntimeInformation
{
    @Override
    public LoaderType getModLoader()
    {
        return LoaderType.FABRIC;
    }

    @Override
    public SideType getEnvironmentType()
    {
        EnvType side = FabricLoader.getInstance().getEnvironmentType();
        return side == EnvType.CLIENT ? SideType.CLIENT : SideType.SERVER;
    }

    @Override
    public String getGameVersion()
    {
        return FabricLoader.getInstance()
            .getRawGameVersion();
    }

    @Override
    public Path getWorkingDirectory()
    {
        return FabricLoader.getInstance()
            .getGameDir();
    }

    @Override
    public Path getConfigDirectory()
    {
        return FabricLoader.getInstance()
            .getConfigDir();
    }

    @Override
    public boolean isDevelopmentEnvironment()
    {
        return FabricLoader.getInstance()
            .isDevelopmentEnvironment();
    }
}
