package io.pastework.core.api.common.event;

public final class Event7<T0, T1, T2, T3, T4, T5, T6> extends AbstractEvent<Event7.Listener<T0, T1, T2, T3, T4, T5, T6>>
{
    public void fire(T0 arg0, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6)
    {
        super.invokeListeners(it -> it.invoke(arg0, arg1, arg2, arg3, arg4, arg5, arg6));
    }

    public interface Listener<_T0, _T1, _T2, _T3, _T4, _T5, _T6>
    {
        void invoke(_T0 arg0, _T1 arg1, _T2 arg2, _T3 arg3, _T4 arg4, _T5 arg5, _T6 arg6);
    }
}
