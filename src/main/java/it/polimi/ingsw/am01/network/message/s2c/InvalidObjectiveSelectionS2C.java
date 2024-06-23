package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells the player that the chosen secret {@code Objective} is not valid.
 * @param objectiveId the id of the objective chosen
 * @see it.polimi.ingsw.am01.network.message.c2s.SelectSecretObjectiveC2S
 * @see it.polimi.ingsw.am01.model.objective.Objective
 */
public record InvalidObjectiveSelectionS2C(int objectiveId) implements S2CNetworkMessage {
    public static final String ID = "INVALID_OBJECTIVE_SELECTION";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
