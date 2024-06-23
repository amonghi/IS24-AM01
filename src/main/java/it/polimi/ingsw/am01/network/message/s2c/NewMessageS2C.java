package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.chat.MessageType;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells the player that he have received a new {@code Message}.
 * @param messageType the type of the message
 * @param timestamp the timestamp of the message
 * @param sender the sender of the message
 * @param content the content of the message
 * @see it.polimi.ingsw.am01.network.message.c2s.SendDirectMessageC2S
 * @see it.polimi.ingsw.am01.network.message.c2s.SendBroadcastMessageC2S
 * @see it.polimi.ingsw.am01.model.chat.Message
 */
public record NewMessageS2C(MessageType messageType, String timestamp, String sender,
                            String content) implements S2CNetworkMessage {
    public static final String ID = "NEW_MESSAGE";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
