package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.network.NetworkException;
import it.polimi.ingsw.am01.network.message.C2SMessageVisitor;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

public record PlaceCardC2S(int cardId, Side side, int i, int j) implements C2SNetworkMessage {
    public static final String ID = "PLACE_CARD";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void accept(C2SMessageVisitor visitor) throws IllegalMoveException, NetworkException {
        visitor.visit(this);
    }
}
