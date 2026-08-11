// Auto Generated code
package io.pastework.core.api.common.event;

public final class Event13<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> extends AbstractEvent<Event13.Listener<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>>
{
    public void fire(T0 arg0, T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9, T10 arg10, T11 arg11, T12 arg12)
    {
        super.invokeListeners(it -> it.invoke(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12));
    }

    public interface Listener<_T0, _T1, _T2, _T3, _T4, _T5, _T6, _T7, _T8, _T9, _T10, _T11, _T12>
    {
        void invoke(_T0 arg0, _T1 arg1, _T2 arg2, _T3 arg3, _T4 arg4, _T5 arg5, _T6 arg6, _T7 arg7, _T8 arg8, _T9 arg9, _T10 arg10, _T11 arg11, _T12 arg12);
    }
}
