package io.pastework.core.base.common.impl.service.network;

import io.pastework.core.api.common.service.network.INetworkConfigurator;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.server.network.ConfigurationTask;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractNetworkConfigurator implements INetworkConfigurator
{
    private final Set<ConfigurationTask> configurationTaskSet = new HashSet<>();
    private final Connection connection;
    private final ServerConfigurationPacketListener listener;

    protected AbstractNetworkConfigurator(Connection connection, ServerConfigurationPacketListener listener)
    {
        this.connection = connection;
        this.listener = listener;
    }

    @Override
    public Connection getConnection()
    {
        return connection;
    }

    @Override
    public ServerConfigurationPacketListener getListener()
    {
        return listener;
    }

    @Override
    public void registerTask(@NonNull ConfigurationTask task)
    {
        configurationTaskSet.add(task);
    }

    @Override
    public Collection<ConfigurationTask> getRegisteredTasks()
    {
        return Collections.unmodifiableSet(configurationTaskSet);
    }

    @Override
    public abstract boolean canAccept(CustomPacketPayload.@NonNull Type<?> type);
}
