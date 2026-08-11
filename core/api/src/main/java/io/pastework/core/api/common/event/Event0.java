package io.pastework.core.api.common.event;

public final class Event0 extends AbstractEvent<Event0.Listener>
{
    public void fire()
    {
        super.invokeListeners(Listener::invoke);
    }

    public interface Listener
    {
        void invoke();
    }
}
