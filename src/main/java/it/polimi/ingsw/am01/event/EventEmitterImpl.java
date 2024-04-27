package it.polimi.ingsw.am01.event;

import java.util.ArrayList;
import java.util.List;

public class EventEmitterImpl<E extends Event> implements EventEmitter<E> {

    private interface RegistrationInternal<V extends Event> extends Registration {
        void notifyIfInterested(V event);
    }

    private final List<RegistrationInternal<E>> registrations;

    public EventEmitterImpl() {
        this.registrations = new ArrayList<>();
    }

    @Override
    public EventEmitter.Registration onAny(EventListener<E> listener) {
        RegistrationInternal<E> registration = listener::onEvent;
        this.registrations.add(registration);
        return registration;
    }

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

    @Override
    public boolean unregister(Registration registration) {
        //noinspection SuspiciousMethodCalls
        return this.registrations.remove(registration);
    }

    public void emit(E event) {
        for (RegistrationInternal<E> registration : this.registrations) {
            registration.notifyIfInterested(event);
        }
    }

    public <T extends E> void bubble(EventEmitter<T> emitter) {
        emitter.onAny(this::emit);
    }
}
