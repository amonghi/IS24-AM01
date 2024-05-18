package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.Map;

public record GameFinishedS2C(Map<String, Integer> finalScores) implements S2CNetworkMessage {
    public static final String ID = "GAME_FINISHED";

    @Override
    public String getId() {
        return ID;
    }
}
