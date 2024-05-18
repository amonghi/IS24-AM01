package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

public record DrawCardFromDeckC2S(DeckLocation deckLocation) implements C2SNetworkMessage {

    public static final String ID = "DRAW_CARD_FROM_DECK";

    @Override
    public String getId() {
        return ID;
    }
}
