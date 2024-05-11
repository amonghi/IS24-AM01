package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.DrawSource;

public record CardDrawnFromEmptySourceEvent(DrawSource drawSource) implements GameEvent {
}
