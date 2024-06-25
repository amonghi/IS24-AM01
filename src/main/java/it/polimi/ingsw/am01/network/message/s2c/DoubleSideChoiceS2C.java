package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message is sent to the player when he tries to choose the starting card side more than once.
 * @param invalidSideChoice the side chosen
 * @see it.polimi.ingsw.am01.network.message.c2s.SelectStartingCardSideC2S
 */
public record DoubleSideChoiceS2C(Side invalidSideChoice) implements S2CNetworkMessage {
    public static final String ID = "DOUBLE_SIDE_CHOICE";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
