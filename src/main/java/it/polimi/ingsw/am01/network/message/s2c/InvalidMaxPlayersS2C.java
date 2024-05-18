package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CMessageVisitor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record InvalidMaxPlayersS2C(int refusedMaxPlayers) implements S2CNetworkMessage {
    public static final String ID = "INVALID_MAX_PLAYERS";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void accept(S2CMessageVisitor visitor) {
        visitor.visit(this);
    }
}
