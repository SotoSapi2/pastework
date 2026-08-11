package io.pastework.core.api.common.event;

public final class Event4<T0, T1, T2, T3> extends AbstractEvent<Event4.Listener<T0, T1, T2, T3>>
{
    public void fire(T0 arg0, T1 arg1, T2 arg2, T3 arg3)
    {
        super.invokeListeners(it -> it.invoke(arg0, arg1, arg2, arg3));
    }

    public interface Listener<_T0, _T1, _T2, _T3>
    {
        void invoke(_T0 arg0, _T1 arg1, _T2 arg2, _T3 arg3);
    }
}
