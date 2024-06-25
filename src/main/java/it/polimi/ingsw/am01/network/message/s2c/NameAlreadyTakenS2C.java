package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells the player that the specified nickname is invalid because it is already used.
 * @param refusedName the refused nickname
 * @see it.polimi.ingsw.am01.network.message.c2s.AuthenticateC2S
 */
public record NameAlreadyTakenS2C(String refusedName) implements S2CNetworkMessage {
    public static final String ID = "NAME_ALREADY_TAKEN";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
