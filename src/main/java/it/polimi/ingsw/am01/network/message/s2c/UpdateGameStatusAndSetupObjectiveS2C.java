package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells the player the two secret {@code Objective}s assigned to him.
 * Player will choose one of them.
 * This message is sent after all players have chosen their personal color.
 * @param objectiveId1 the id of the first objective
 * @param objectiveId2 the id of the second objective
 * @see it.polimi.ingsw.am01.model.event.AllColorChoicesSettledEvent
 */
public record UpdateGameStatusAndSetupObjectiveS2C(int objectiveId1, int objectiveId2) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_GAME_STATUS_AND_SETUP_OBJECTIVE";
    private static final GameStatus gameStatus = GameStatus.SETUP_OBJECTIVE;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }

    /**
     * @return the current game's status
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
