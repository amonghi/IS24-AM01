package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.List;
import java.util.Map;

public record UpdateGameListS2C(Map<Integer, List<Integer>> gameList) implements S2CNetworkMessage { //TODO: change with Pair<currentPlayersConnected, maxPlayers>
    public static final String ID = "UPDATE_GAME_LIST_INFO";

    @Override
    public String getId() {
        return ID;
    }
}
