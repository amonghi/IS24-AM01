package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells the player that the recipient he specified is invalid (recipient is not in the same game as the sender).
 * @param invalidRecipientName the recipient name specified
 * @see it.polimi.ingsw.am01.network.message.c2s.SendDirectMessageC2S
 * @see it.polimi.ingsw.am01.network.message.c2s.SendBroadcastMessageC2S
 */
public record InvalidRecipientS2C(String invalidRecipientName) implements S2CNetworkMessage {
    public static final String ID = "INVALID_RECIPIENT";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
