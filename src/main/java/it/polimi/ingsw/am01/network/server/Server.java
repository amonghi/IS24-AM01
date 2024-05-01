package it.polimi.ingsw.am01.network.server;

import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public interface Server {
    Connection<S2CNetworkMessage, C2SNetworkMessage> accept() throws OpenConnectionNetworkException;
}
