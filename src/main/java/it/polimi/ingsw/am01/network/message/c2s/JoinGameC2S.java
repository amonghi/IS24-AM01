package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

public record JoinGameC2S(int gameId) implements C2SNetworkMessage {
    public static final String ID = "JOIN_GAME";

    @Override
    public String getId() {
        return ID;
    }

}
