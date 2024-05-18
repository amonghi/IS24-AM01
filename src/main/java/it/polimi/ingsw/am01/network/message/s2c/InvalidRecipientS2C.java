package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record InvalidRecipientS2C(String invalidRecipientName) implements S2CNetworkMessage {
    public static final String ID = "INVALID_RECIPIENT";

    @Override
    public String getId() {
        return ID;
    }
}
