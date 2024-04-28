package it.polimi.ingsw.am01.event;

/**
 * An object that emits events.
 * <p>
 * The events that are can be listened to by {@link EventListener}s.
 * Before an event listener can receive an event it must be registered using {@link #onAny(EventListener)} or
 * {@link #on(Class, EventListener)}.
 * The same {@link EventListener} can be registered more than once.
 * When an event is emitted {@link EventListener}s receive it in the same that they were registered, if a listener is
 * registered multiple times, it will receive the event multiple times.
 *
 * @param <E> the type of events that is emitted by this EventEmitter.
 */
public interface EventEmitter<E extends Event> {
    /**
     * A registration is returned by {@link #onAny(EventListener)} and {@link #on(Class, EventListener)} and can be used
     * to stop listening to events.
     */
    interface Registration {
    }

    /**
     * Registers an {@link EventListener} that will listen to any event that is emitted.
     *
     * @param listener the listener to be registered.
     * @return a {@link Registration} that can later be used to unregister this {@link EventListener}
     * @see #unregister(Registration)
     */
    Registration onAny(EventListener<E> listener);

    /**
     * Registers an {@link EventListener} that will listen only to a certain type of events.
     *
     * @param eventClass the listener will be called only if the emitted event is an instance of this class.
     * @param listener   the listener to be registered.
     * @param <T>        the type of events that the listener will listen to.
     * @return a {@link Registration} that can later be used to unregister this {@link EventListener}
     * @see #unregister(Registration)
     */
    <T extends E> Registration on(Class<T> eventClass, EventListener<T> listener);

    /**
     * Remove a registration from this EventEmitter.
     *
     * @param registration the registration to remove.
     * @return true iff the provided registration was present and has been removed.
     */
    boolean unregister(Registration registration);
}
