package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message is used to inform the player that the {@code DirectMessage} has been sent.
 * @param sender the sender of the message
 * @param recipient the recipient of the message
 * @param content the content of the message
 * @param timestamp the timestamp of the message
 * @see it.polimi.ingsw.am01.network.message.c2s.SendDirectMessageC2S
 * @see it.polimi.ingsw.am01.model.chat.DirectMessage
 */
public record DirectMessageSentS2C(String sender, String recipient, String content,
                                   String timestamp) implements S2CNetworkMessage {
    public static final String ID = "DIRECT_MESSAGE_SENT";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
