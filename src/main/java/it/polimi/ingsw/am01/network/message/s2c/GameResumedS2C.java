package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record GameResumedS2C() implements S2CNetworkMessage {
    public static final String ID = "SET_RECOVER_STATUS";

    @Override
    public String getId() {
        return ID;
    }
}
