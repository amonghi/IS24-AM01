package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.MessageVisitor;

public record PlaceCardC2S(int cardId, Side side, int i, int j) implements C2SNetworkMessage {
    public static final String ID = "PLACE_CARD";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) throws IllegalMoveException {
        messageVisitor.visit(this);
    }
}
