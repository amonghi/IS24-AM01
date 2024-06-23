package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.Set;

/**
 * This message tells players the list of users who have chosen their secret {@code Objective} ({@code SETUP_OBJECTIVE} status).
 * @param playersHaveChosen the list of players
 * @see it.polimi.ingsw.am01.model.event.SecretObjectiveChosenEvent
 */
public record UpdateObjectiveSelectedS2C(Set<String> playersHaveChosen) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_OBJECTIVE_SELECTED";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
