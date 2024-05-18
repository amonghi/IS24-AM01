package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

public record SelectSecretObjectiveC2S(int objective) implements C2SNetworkMessage {

    public static final String ID = "SELECT_SECRET_OBJECTIVE";

    @Override
    public String getId() {
        return ID;
    }
}
