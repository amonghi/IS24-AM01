package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

public record AuthenticateC2S(String playerName) implements C2SNetworkMessage {
    public static final String ID = "AUTHENTICATE";

    @Override
    public String getId() {
        return ID;
    }
}
