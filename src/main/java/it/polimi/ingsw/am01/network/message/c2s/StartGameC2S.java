package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

public record StartGameC2S() implements C2SNetworkMessage {
    public static final String ID = "EARLY_START";

    @Override
    public String getId() {
        return ID;
    }
}
