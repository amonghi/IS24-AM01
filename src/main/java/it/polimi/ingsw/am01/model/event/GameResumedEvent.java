package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.TurnPhase;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

public record GameResumedEvent(GameStatus status, TurnPhase turnPhase,
                               PlayerProfile currentPlayer) implements GameEvent {
}
