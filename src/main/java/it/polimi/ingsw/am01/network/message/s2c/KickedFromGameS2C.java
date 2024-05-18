package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record KickedFromGameS2C() implements S2CNetworkMessage {
    public static final String ID = "KICKED_FROM_GAME";

    @Override
    public String getId() {
        return ID;
    }
}
