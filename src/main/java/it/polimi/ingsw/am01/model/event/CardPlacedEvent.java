package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.PlayArea;

public record CardPlacedEvent(String playerName, PlayArea.CardPlacement cardPlacement) implements GameEvent {
}
