package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells user the id of the starting card assigned to him.
 * This message is sent when the game starts ({@code SETUP_STARTING_CARD_SIDE} status).
 * @param startingCardId the id of the starting card
 * @see it.polimi.ingsw.am01.model.event.AllPlayersJoinedEvent
 */
public record SetStartingCardS2C(int startingCardId) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_STATUS_AND_SET_STARTING_CARD";
    private static final GameStatus gameStatus = GameStatus.SETUP_STARTING_CARD_SIDE;

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
