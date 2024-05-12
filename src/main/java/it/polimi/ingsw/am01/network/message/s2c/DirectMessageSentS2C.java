package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record DirectMessageSentS2C(String sender, String content, String recipient,
                                   String timestamp) implements S2CNetworkMessage {
    public static final String ID = "DIRECT_MESSAGE_SENT";

    @Override
    public String getId() {
        return ID;
    }
}
