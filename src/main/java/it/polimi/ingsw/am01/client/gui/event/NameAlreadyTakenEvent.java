package it.polimi.ingsw.am01.client.gui.event;

public record NameAlreadyTakenEvent(String refusedName) implements ViewEvent {
}
