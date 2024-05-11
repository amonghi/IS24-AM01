package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record GameAlreadyStartedS2C(int gameId) implements S2CNetworkMessage {
    public static final String ID = "GAME_ALREADY_STARTED";

    @Override
    public String getId() {
        return ID;
    }
}
