package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells the user that the chosen name has been accepted.
 * @param playerName the nickname chosen by the player
 * @see it.polimi.ingsw.am01.network.message.c2s.AuthenticateC2S
 */
public record SetPlayerNameS2C(String playerName) implements S2CNetworkMessage {
    public static final String ID = "SET_NAME";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
