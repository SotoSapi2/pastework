package io.pastework.core.neoforge.impl.service.common.network;

import io.pastework.core.api.common.service.network.NetworkThreadType;
import io.pastework.core.base.common.impl.service.network.AbstractPacketContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.HandlerThread;
import org.jspecify.annotations.NonNull;

public final class NeoPacketContext extends AbstractPacketContext
{
    private final IPayloadContext context;
    private final NetworkThreadType networkThreadType;

    public NeoPacketContext(
        IPayloadContext context,
        HandlerThread networkThread
    )
    {
        super(
            context.flow(),
            context.connection(),
            context.listener(),
            context.player()
        );

        this.context = context;
        this.networkThreadType = switch (networkThread)
        {
            case NETWORK -> NetworkThreadType.NETWORK;
            case MAIN -> NetworkThreadType.MAIN;
        };
    }

    @Override
    public NetworkThreadType getNetworkThreadType()
    {
        return networkThreadType;
    }

    @Override
    public void reply(@NonNull CustomPacketPayload payload)
    {
        context.reply(payload);
    }

    @Override
    public boolean canAccept(CustomPacketPayload.@NonNull Type<?> type)
    {
        return context.listener()
            .hasChannel(type);
    }
}
