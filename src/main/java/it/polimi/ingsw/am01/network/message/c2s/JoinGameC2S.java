package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

/**
 * This message is used to join a {@code Game} that has not yet started.
 * @param gameId the id of the game
 * @see it.polimi.ingsw.am01.model.game.GameStatus
 */
public record JoinGameC2S(int gameId) implements C2SNetworkMessage {
    public static final String ID = "JOIN_GAME";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }

}
