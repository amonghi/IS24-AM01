package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message is sent to the player when he tries to choose the secret objective more than once.
 * @see it.polimi.ingsw.am01.network.message.c2s.SelectSecretObjectiveC2S
 * @see it.polimi.ingsw.am01.model.game.DoubleChoiceException
 */
public record DoubleChoiceS2C() implements S2CNetworkMessage {
    public static final String ID = "DOUBLE_CHOICE";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
