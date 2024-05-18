package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CMessageVisitor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.io.Serializable;
import java.util.Map;

public record UpdateGameListS2C(Map<Integer, GameStat> gamesStatMap) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_GAME_LIST_INFO";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void accept(S2CMessageVisitor visitor) {
        visitor.visit(this);
    }

    public record GameStat(int currentPlayersConnected, int maxPlayers) implements Serializable {
    }
}
