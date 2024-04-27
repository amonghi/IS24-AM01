package it.polimi.ingsw.am01.event;

public interface EventEmitter<E extends Event> {
    interface Registration {
    }

    Registration onAny(EventListener<E> listener);

    <T extends E> Registration on(Class<T> eventClass, EventListener<T> listener);

    boolean unregister(Registration registration);
}
