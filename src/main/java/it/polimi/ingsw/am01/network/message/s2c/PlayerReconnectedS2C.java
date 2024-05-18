package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record PlayerReconnectedS2C(String playerName) implements S2CNetworkMessage {

    public static final String ID = "PLAYER_RECONNECTED";

    @Override
    public String getId() {
        return ID;
    }
}
