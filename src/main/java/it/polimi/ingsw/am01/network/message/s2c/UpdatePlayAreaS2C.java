package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record UpdatePlayAreaS2C(String playerName, int i, int j, int cardId, Side side, int seq,
                                int points) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_PLAY_AREA";

    @Override
    public String getId() {
        return ID;
    }
}
