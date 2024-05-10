package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Game;

public record GameCreatedEvent(Game newGame) implements GameManagerEvent {
}
