package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

/**
 * This message is used to instantly start a game,
 * without having to reach the maximum capacity of the game.
 */
public record StartGameC2S() implements C2SNetworkMessage {
    public static final String ID = "EARLY_START";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
