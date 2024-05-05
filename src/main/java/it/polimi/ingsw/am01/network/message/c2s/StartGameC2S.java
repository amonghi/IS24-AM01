package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.MessageVisitor;

public record StartGameC2S() implements C2SNetworkMessage {
    public static final String ID = "EARLY_START";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void accept(MessageVisitor messageVisitor) throws IllegalMoveException {
        messageVisitor.visit(this);
    }
}
