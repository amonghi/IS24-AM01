package it.polimi.ingsw.am01.client.gui.event;

public record InvalidStartGameRequestEvent(int actualPlayers) implements ViewEvent {
}
