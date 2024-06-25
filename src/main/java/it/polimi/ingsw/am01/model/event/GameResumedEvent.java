package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.TurnPhase;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted when a game is resumed by the server.
 * @param status the current game's status
 * @param turnPhase the current turn phase
 * @param currentPlayer the current player
 * @see Game#resumeGame()
 */
public record GameResumedEvent(GameStatus status, TurnPhase turnPhase,
                               PlayerProfile currentPlayer) implements GameEvent {
}
