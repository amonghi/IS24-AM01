package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.TurnPhase;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

public record UpdateGameStatusAndTurnEvent(GameStatus gameStatus, TurnPhase turnPhase,
                                           PlayerProfile currentPlayer) implements GameEvent {
}
