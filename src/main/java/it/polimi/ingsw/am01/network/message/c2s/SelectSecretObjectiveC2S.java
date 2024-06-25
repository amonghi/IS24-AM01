package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

/**
 * This message is used to select a secret objective during {@code SETUP_OBJECTIVE} status.
 * @param objective the id of the objective chosen
 * @see it.polimi.ingsw.am01.model.game.GameStatus
 */
public record SelectSecretObjectiveC2S(int objective) implements C2SNetworkMessage {
    public static final String ID = "SELECT_SECRET_OBJECTIVE";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
