package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

/**
 * This message is used to place a {@code Card} in the {@code PlayArea}.
 * @param cardId the card id
 * @param side the visible side of the card
 * @param i the i coordinate related to the {@code PlayArea}
 * @param j the j coordinate related to the {@code PlayArea}
 * @see it.polimi.ingsw.am01.model.game.PlayArea
 * @see it.polimi.ingsw.am01.model.card.Card
 */
public record PlaceCardC2S(int cardId, Side side, int i, int j) implements C2SNetworkMessage {
    public static final String ID = "PLACE_CARD";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
