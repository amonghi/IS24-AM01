package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CMessageVisitor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.io.Serializable;
import java.util.List;

public record SetPlayablePositionsS2C(List<PlayablePosition> playablePositions) implements S2CNetworkMessage {
    public static final String ID = "SET_PLAYABLE_POSITIONS";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void accept(S2CMessageVisitor visitor) {
        visitor.visit(this);
    }

    public record PlayablePosition(int i, int j) implements Serializable {
    }
}
