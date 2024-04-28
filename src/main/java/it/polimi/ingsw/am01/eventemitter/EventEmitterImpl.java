package it.polimi.ingsw.am01.eventemitter;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of {@link EventEmitter} that other classes can delegate to.
 *
 * @param <E> the type of events that is emitted by this EventEmitter.
 */
public class EventEmitterImpl<E extends Event> implements EventEmitter<E> {

    private interface RegistrationInternal<V extends Event> extends Registration {
        void notifyIfInterested(V event);
    }

    private final List<RegistrationInternal<E>> registrations;

    public EventEmitterImpl() {
        this.registrations = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventEmitter.Registration onAny(EventListener<E> listener) {
        RegistrationInternal<E> registration = listener::onEvent;
        this.registrations.add(registration);
        return registration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends E> Registration on(Class<T> eventClass, EventListener<T> listener) {
        RegistrationInternal<E> registration = event -> {
            if (eventClass.isInstance(event)) {
                T castEvent = eventClass.cast(event);
                listener.onEvent(castEvent);
            }
        };
        this.registrations.add(registration);
        return registration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unregister(Registration registration) {
        //noinspection SuspiciousMethodCalls
        return this.registrations.remove(registration);
    }

    /**
     * Emit an event.
     *
     * @param event the event to emit.
     */
    public void emit(E event) {
        for (RegistrationInternal<E> registration : this.registrations) {
            registration.notifyIfInterested(event);
        }
    }

    /**
     * Re-emit all events that come from the source event emitter.
     *
     * @param source source of the events to re-emit.
     * @param <T>    the type of the events emitted from the source.
     */
    public <T extends E> void bubble(EventEmitter<T> source) {
        source.onAny(this::emit);
    }
}
