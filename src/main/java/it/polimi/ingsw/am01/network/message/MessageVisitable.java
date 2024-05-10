package it.polimi.ingsw.am01.network.message;

import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.network.NetworkException;

public interface MessageVisitable {
    void accept(MessageVisitor messageVisitor) throws IllegalMoveException, NetworkException;
}
