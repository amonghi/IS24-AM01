package it.polimi.ingsw.am01.network.message;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.controller.ProtocolState;
import it.polimi.ingsw.am01.network.Connection;

public interface C2SNetworkMessage extends NetworkMessage {
    void execute(ProtocolState state, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, Controller controller);
}
