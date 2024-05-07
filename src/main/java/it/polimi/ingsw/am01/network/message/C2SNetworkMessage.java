package it.polimi.ingsw.am01.network.message;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.NetworkException;
import it.polimi.ingsw.am01.network.ProtocolState;

public interface C2SNetworkMessage extends NetworkMessage {
    void execute(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, ProtocolState state) throws NetworkException;
}
