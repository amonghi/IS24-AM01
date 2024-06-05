package it.polimi.ingsw.am01.client.gui.event;

public record UpdateGameTurnEvent(String player, String currentPlayer, String turnPhase,
                                  String gameStatus) implements ViewEvent {
}
