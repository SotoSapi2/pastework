package io.pastework.core.neoforge.impl.spi;

import io.pastework.core.api.common.platform.LoaderType;
import io.pastework.core.api.common.platform.SideType;
import io.pastework.core.api.spi.IRuntimeInformation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class NeoRuntimeInformation implements IRuntimeInformation
{
    @Override
    public LoaderType getModLoader()
    {
        return LoaderType.NEOFORGE;
    }

    @Override
    public SideType getEnvironmentType()
    {
        Dist side = FMLLoader.getCurrent().getDist();
        return side == Dist.CLIENT ? SideType.CLIENT : SideType.SERVER;
    }

    @Override
    public String getGameVersion()
    {
        return FMLLoader.getCurrent()
            .getVersionInfo()
            .mcVersion();
    }

    @Override
    public Path getWorkingDirectory()
    {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public Path getConfigDirectory()
    {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean isDevelopmentEnvironment()
    {
        return !FMLLoader.getCurrent()
            .isProduction();
    }
}
