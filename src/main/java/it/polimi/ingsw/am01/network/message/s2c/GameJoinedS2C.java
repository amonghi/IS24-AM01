package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells player that he has joined a game.
 * The message also contains the current {@code GameStatus} of the game.
 * @param gameId the game id
 * @param gameStatus the game status
 * @see it.polimi.ingsw.am01.network.message.c2s.JoinGameC2S
 * @see GameStatus
 */
public record GameJoinedS2C(int gameId, GameStatus gameStatus) implements S2CNetworkMessage {
    public static final String ID = "GAME_JOINED";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
