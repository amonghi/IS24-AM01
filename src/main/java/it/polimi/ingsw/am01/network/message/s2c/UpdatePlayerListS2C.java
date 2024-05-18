package it.polimi.ingsw.am01.network.message.s2c;


import it.polimi.ingsw.am01.network.message.S2CMessageVisitor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.List;

public record UpdatePlayerListS2C(List<String> playerList) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_PLAYER_LIST";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void accept(S2CMessageVisitor visitor) {
        visitor.visit(this);
    }
}
