package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells the player that the card placement is not valid.
 * @param cardId the id of the card to be placed
 * @param side the side of the card to be placed
 * @param i the i coordinate of the card to be placed
 * @param j the j coordinate of the card to be placed
 * @see it.polimi.ingsw.am01.network.message.c2s.PlaceCardC2S
 * @see it.polimi.ingsw.am01.model.game.Game
 * @see it.polimi.ingsw.am01.model.exception.IllegalPlacementException
 */
public record InvalidPlacementS2C(int cardId, Side side, int i, int j) implements S2CNetworkMessage {
    public static final String ID = "INVALID_PLACEMENT";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
