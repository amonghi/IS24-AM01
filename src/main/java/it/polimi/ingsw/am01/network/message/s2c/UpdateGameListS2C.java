package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.Map;

public record UpdateGameListS2C(Map<Integer, Integer> gameList) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_GAME_LIST";

    @Override
    public String getId() {
        return ID;
    }
}
