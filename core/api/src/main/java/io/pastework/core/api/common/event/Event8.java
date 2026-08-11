package io.pastework.core.api.common.event;

public final class Event8<T0, T1, T2, T3, T4, T5, T6, T7> extends AbstractEvent<Event8.Listener<T0, T1, T2, T3, T4, T5, T6, T7>>
{
    public void fire(T0 arg0, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7)
    {
        super.invokeListeners(it -> it.invoke(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7));
    }

    public interface Listener<_T0, _T1, _T2, _T3, _T4, _T5, _T6, _T7>
    {
        void invoke(_T0 arg0, _T1 arg1, _T2 arg2, _T3 arg3, _T4 arg4, _T5 arg5, _T6 arg6, _T7 arg7);
    }
}
