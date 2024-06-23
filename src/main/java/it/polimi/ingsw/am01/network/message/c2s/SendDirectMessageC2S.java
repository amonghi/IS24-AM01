package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

/**
 * This message is used to send a {@code DirectMessage}.
 * @param recipientPlayerName the name of the recipient
 * @param content the content of the message
 * @see it.polimi.ingsw.am01.model.chat.DirectMessage
 */
public record SendDirectMessageC2S(String recipientPlayerName, String content) implements C2SNetworkMessage {
    public static final String ID = "SEND_DIRECT_MESSAGE";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
