package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record NameAlreadyTakenS2C(String refusedName) implements S2CNetworkMessage {
    private static final String ID = "NAME_ALREADY_TAKEN";

    @Override
    public String getId() {
        return ID;
    }
}
