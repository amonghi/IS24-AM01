package it.polimi.ingsw.am01.event;

public interface EventListener<E extends Event> {
    void onEvent(E event);
}
