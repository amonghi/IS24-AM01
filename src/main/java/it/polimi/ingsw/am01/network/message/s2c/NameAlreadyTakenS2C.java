package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.json.NetworkMessageTypeAdapterFactory;

public record NameAlreadyTakenS2C(String refusedName) implements S2CNetworkMessage {
    public static final String ID = "NAME_ALREADY_TAKEN";

    static {
        NetworkMessageTypeAdapterFactory.register(ID, NameAlreadyTakenS2C.class);
    }

    @Override
    public String getId() {
        return ID;
    }
}
