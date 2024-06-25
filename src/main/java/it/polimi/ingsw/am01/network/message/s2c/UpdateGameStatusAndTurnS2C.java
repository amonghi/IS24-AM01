package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.TurnPhase;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells players the new status of the game: {@code GameStatus}, {@code TurnPhase} and current player.
 * @param gameStatus the current game's status
 * @param turnPhase the current turn phase
 * @param currentPlayerName the current player
 * @see it.polimi.ingsw.am01.model.event.UpdateGameStatusAndTurnEvent
 * @see it.polimi.ingsw.am01.model.event.GameResumedEvent
 */
public record UpdateGameStatusAndTurnS2C(GameStatus gameStatus, TurnPhase turnPhase,
                                         String currentPlayerName) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_GAME_STATUS_AND_TURN";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
