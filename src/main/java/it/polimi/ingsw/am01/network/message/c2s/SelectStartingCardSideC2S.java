package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.network.NetworkException;
import it.polimi.ingsw.am01.network.message.C2SMessageVisitor;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;

public record SelectStartingCardSideC2S(Side side) implements C2SNetworkMessage {
    public static final String ID = "SELECT_STARTING_CARD_SIDE";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void accept(C2SMessageVisitor visitor) throws IllegalMoveException, NetworkException {
        visitor.visit(this);
    }
}
