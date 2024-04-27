package it.polimi.ingsw.am01.event;

public interface EventListener<E extends Event> {
    void notify(E event);
}
