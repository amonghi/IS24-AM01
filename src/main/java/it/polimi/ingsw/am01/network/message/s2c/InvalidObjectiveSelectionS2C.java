package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public record InvalidObjectiveSelectionS2C(int objectiveId) implements S2CNetworkMessage {

    public static final String ID = "INVALID_OBJECTIVE_SELECTION";

    @Override
    public String getId() {
        return ID;
    }
}
