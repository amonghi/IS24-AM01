package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

public record CreateGameAndJoinC2S(int maxPlayers) implements C2SNetworkMessage {
    public static final String ID = "CREATE_GAME_AND_JOIN";

    @Override
    public String getId() {
        return ID;
    }
}
