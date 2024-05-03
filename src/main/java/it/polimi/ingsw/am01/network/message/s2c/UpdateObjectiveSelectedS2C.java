package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.Set;

public record UpdateObjectiveSelectedS2C(Set<String> playersHaveChosen) implements S2CNetworkMessage {

    public static final String ID = "UPDATE_OBJECTIVE_SELECTED";

    @Override
    public String getId() {
        return ID;
    }
}
