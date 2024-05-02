package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record InvalidGameStateS2C() implements S2CNetworkMessage {
    public static final String ID = "WRONG_GAME_STATE";

    @Override
    public String getId() {
        return ID;
    }
}
