package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

/**
 * This message is used to draw a card from the specified deck type.
 * @param deckLocation the deck type from which to draw the card
 * @see DeckLocation
 */
public record DrawCardFromDeckC2S(DeckLocation deckLocation) implements C2SNetworkMessage {
    public static final String ID = "DRAW_CARD_FROM_DECK";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
