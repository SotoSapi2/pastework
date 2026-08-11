package io.pastework.core.api.common.event;

public final class Event3<T0, T1, T2> extends AbstractEvent<Event3.Listener<T0, T1, T2>>
{
    public void fire(T0 arg0, T1 arg1, T2 arg2)
    {
        super.invokeListeners(it -> it.invoke(arg0, arg1, arg2));
    }

    public interface Listener<_T0, _T1, _T2>
    {
        void invoke(_T0 arg0, _T1 arg1, _T2 arg2);
    }
}
