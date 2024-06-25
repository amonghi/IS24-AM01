package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells players the new visible colors of the two decks.
 * This message is sent whenever a user draws from one of the two decks.
 * If a deck is empty, the {@code null} value will be sent.
 * @param resourceDeckColor the visible color of resource deck
 * @param goldenDeckColor the visible color of golden deck
 * @see it.polimi.ingsw.am01.model.event.CardDrawnFromDeckEvent
 */
public record UpdateDeckStatusS2C(CardColor resourceDeckColor,
                                  CardColor goldenDeckColor) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_DECK_STATUS";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
