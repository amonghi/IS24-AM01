package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message is used to inform the player that the {@code BroadcastMessage} has been sent.
 * @param sender the sender of the message
 * @param content the content of the message
 * @param timestamp the timestamp of the message
 * @see it.polimi.ingsw.am01.network.message.c2s.SendBroadcastMessageC2S
 * @see it.polimi.ingsw.am01.model.chat.BroadcastMessage
 */
public record BroadcastMessageSentS2C(String sender, String content, String timestamp) implements S2CNetworkMessage {
    public static final String ID = "BROADCAST_MESSAGE_SENT";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
