package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

/**
 * This message is used to select the starting card's side during {@code SETUP_STARTING_CARD_SIDE} status.
 * @param side the side chosen
 * @see it.polimi.ingsw.am01.model.game.GameStatus
 */
public record SelectStartingCardSideC2S(Side side) implements C2SNetworkMessage {
    public static final String ID = "SELECT_STARTING_CARD_SIDE";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
