package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message is used to inform the player that the specified {@code Game} has already started.
 * @param gameId the id of the game
 * @see it.polimi.ingsw.am01.model.game.GameStatus
 */
public record GameAlreadyStartedS2C(int gameId) implements S2CNetworkMessage {
    public static final String ID = "GAME_ALREADY_STARTED";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
