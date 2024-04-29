package it.polimi.ingsw.am01.network.server;

import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.io.IOException;

public interface Server {
    Connection<S2CNetworkMessage, C2SNetworkMessage> accept() throws IOException;
}
