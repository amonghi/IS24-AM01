package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.GameStatus;

public record GameResumedEvent(GameStatus recoverStatus) implements GameEvent {
}
