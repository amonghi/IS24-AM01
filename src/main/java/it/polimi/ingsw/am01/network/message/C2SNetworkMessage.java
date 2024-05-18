package it.polimi.ingsw.am01.network.message;

import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.network.NetworkException;

public interface C2SNetworkMessage extends NetworkMessage {
    void accept(C2SMessageVisitor visitor) throws IllegalMoveException, NetworkException;
}
