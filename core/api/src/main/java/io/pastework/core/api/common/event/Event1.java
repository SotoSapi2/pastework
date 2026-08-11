package io.pastework.core.api.common.event;

public final class Event1<T0> extends AbstractEvent<Event1.Listener<T0>>
{
    public void fire(T0 arg0)
    {
        super.invokeListeners(it -> it.invoke(arg0));
    }

    public interface Listener<_T0>
    {
        void invoke(_T0 arg0);
    }
}
