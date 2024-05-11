package it.polimi.ingsw.am01.eventemitter;

/**
 * A listener is an object that can receive events emitted by an {@link EventEmitter}.
 * <p>
 * To receive these events the listener must be registered to the {@link EventEmitter} through
 * {@link EventEmitter#onAny(EventListener)} or {@link EventEmitter#on(Class, EventListener)}.
 *
 * @param <E> the type of event that this listener listens to.
 */
public interface EventListener<E extends Event> {

    /**
     * This method is invoked when an event is emitted.
     *
     * @param event the event that has been emitted.
     */
    void onEvent(E event);
}
