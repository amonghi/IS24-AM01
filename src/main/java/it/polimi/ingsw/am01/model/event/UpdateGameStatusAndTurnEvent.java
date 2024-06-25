package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.TurnPhase;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted when:
 * <li>the current player changes;</li>
 * <li>the {@link TurnPhase} changes;</li>
 * <li>the {@link GameStatus} changes.</li>
 * @param gameStatus the new game's status
 * @param turnPhase the new turn phase
 * @param currentPlayer the new current player
 */
public record UpdateGameStatusAndTurnEvent(GameStatus gameStatus, TurnPhase turnPhase,
                                           PlayerProfile currentPlayer) implements GameEvent {
}
