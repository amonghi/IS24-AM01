package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

/**
 * This message is used to send a {@code BroadcastMessage}.
 * @param content the content of the message
 * @see it.polimi.ingsw.am01.model.chat.BroadcastMessage
 */
public record SendBroadcastMessageC2S(String content) implements C2SNetworkMessage {
    public static final String ID = "SEND_BROADCAST_MESSAGE";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
