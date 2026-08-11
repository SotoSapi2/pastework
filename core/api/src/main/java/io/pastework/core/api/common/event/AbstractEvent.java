package io.pastework.core.api.common.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An abstract base class for implementing event.
 *
 * @param <TListener> The type of the listener interface that this event handles.
 */
public abstract class AbstractEvent<TListener>
{
    private final Lock lock = new ReentrantLock();
    private final List<Connection<TListener>> listenerList = new ArrayList<>();

    /**
     * Represents a functional interface used to invoke a specific listener.
     *
     * @param <_TListener> The type of the listener.
     */
    @FunctionalInterface
    public interface Invoker<_TListener>
    {
        /**
         * Handles the invocation of the listener.
         *
         * @param listener The listener to invoke.
         */
        void handle(_TListener listener);
    }

    private static final class Connection<_TListener> implements IEventConnection
    {
        private final int priority;
        private final _TListener listener;
        private final AbstractEvent<_TListener> event;
        private volatile boolean isConnected = true;

        private Connection(int priority, _TListener listener, AbstractEvent<_TListener> event)
        {
            this.priority = priority;
            this.listener = listener;
            this.event = event;
        }

        @Override
        public int getPriority()
        {
            return priority;
        }

        @Override
        public void disconnect()
        {
            if (isConnected)
            {
                event.disconnect(listener);
                isConnected = true;
            }
        }

        @Override
        public boolean isConnected()
        {
            return isConnected;
        }
    }

    /**
     * Connects a listener to this event with a specified priority.
     * Listeners with higher priorities are invoked first. See {@link EventPriority} for standard priorities.
     *
     * @param priority The priority of the listener.
     * @param listener The listener to be connected.
     * @return An {@link IEventConnection} representing the connection.
     * @throws NullPointerException if the listener is null.
     */
    public final IEventConnection connect(int priority, TListener listener)
    {
        Objects.requireNonNull(listener);

        synchronized (lock)
        {
            var connection = new Connection<>(priority, listener, this);

            listenerList.add(connection);
            listenerList.sort(this::handleComparison);

            return connection;
        }
    }

    /**
     * Connects a listener to this event with the default priority ({@link EventPriority#NORMAL}).
     *
     * @param listener The listener to be connected.
     * @return An {@link IEventConnection} representing the connection.
     * @throws NullPointerException if the listener is null.
     */
    public final IEventConnection connect(TListener listener)
    {
        Objects.requireNonNull(listener);
        return connect(EventPriority.NORMAL, listener);
    }

    /**
     * Disconnects a previously connected listener from this event.
     *
     * @param listener The listener to disconnect.
     * @return {@code true} if the listener was successfully removed, {@code false} otherwise.
     */
    public final boolean disconnect(TListener listener)
    {
        synchronized (lock)
        {
            boolean removed = listenerList.removeIf(it -> it.listener == listener);
            listenerList.sort(this::handleComparison);

            return removed;
        }
    }

    /**
     * Invokes all registered listeners using the provided invoker.
     * The listeners are invoked in descending order of their priority.
     *
     * @param invoker The invoker defining how to handle each listener.
     */
    protected final void invokeListeners(Invoker<TListener> invoker)
    {
        synchronized (lock)
        {
            for (Connection<TListener> connection : listenerList)
            {
                invoker.handle(connection.listener);
            }
        }
    }

    private int handleComparison(IEventConnection left, IEventConnection right)
    {
        return -Integer.compare(left.getPriority(), right.getPriority());
    }
}