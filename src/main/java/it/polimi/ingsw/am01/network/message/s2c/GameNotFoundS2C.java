package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CMessageVisitor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record GameNotFoundS2C(int refusedGameId) implements S2CNetworkMessage {
    public static final String ID = "GAME_NOT_FOUND";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void accept(S2CMessageVisitor visitor) {
        visitor.visit(this);
    }
}
