package it.polimi.ingsw.am01.network.message;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.controller.VirtualView;
import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.network.Connection;

public interface C2SNetworkMessage extends NetworkMessage {
    void execute(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, VirtualView virtualView) throws IllegalMoveException;
}
