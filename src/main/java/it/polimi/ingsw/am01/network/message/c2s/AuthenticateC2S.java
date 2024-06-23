package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

/**
 * This message is used to authenticate a player,
 * allowing the user to specify a nickname (unique).
 * @param playerName the nickname chosen by the player.
 */
public record AuthenticateC2S(String playerName) implements C2SNetworkMessage {
    public static final String ID = "AUTHENTICATE";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
