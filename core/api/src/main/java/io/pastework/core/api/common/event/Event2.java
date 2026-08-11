package io.pastework.core.api.common.event;

public final class Event2<T0, T1> extends AbstractEvent<Event2.Listener<T0, T1>>
{
    public void fire(T0 arg0, T1 arg1)
    {
        super.invokeListeners(it -> it.invoke(arg0, arg1));
    }

    public interface Listener<_T0, _T1>
    {
        void invoke(_T0 arg0, _T1 arg1);
    }
}
