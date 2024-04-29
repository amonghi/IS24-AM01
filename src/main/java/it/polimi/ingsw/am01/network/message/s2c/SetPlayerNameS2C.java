package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.json.NetworkMessageTypeAdapterFactory;

public record SetPlayerNameS2C(String playerName) implements S2CNetworkMessage {
    public static final String ID = "SET_NAME";

    static {
        NetworkMessageTypeAdapterFactory.register(ID, SetPlayerNameS2C.class);
    }

    @Override
    public String getId() {
        return ID;
    }
}
