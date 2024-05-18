package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record InvalidCardS2C(int cardId) implements S2CNetworkMessage {
    public static final String ID = "INVALID_CARD";

    @Override
    public String getId() {
        return ID;
    }
}
