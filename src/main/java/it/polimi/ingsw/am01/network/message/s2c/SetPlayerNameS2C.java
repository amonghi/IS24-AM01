package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record SetPlayerNameS2C(String playerName) implements S2CNetworkMessage {
    public static final String ID = "SET_NAME";

    @Override
    public String getId() {
        return ID;
    }
}
