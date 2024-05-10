package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.network.NetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.MessageVisitor;

public record DrawCardFromDeckC2S(DeckLocation deckLocation) implements C2SNetworkMessage {

    public static final String ID = "DRAW_CARD_FROM_DECK";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) throws IllegalMoveException, NetworkException {
        messageVisitor.visit(this);
    }
}
