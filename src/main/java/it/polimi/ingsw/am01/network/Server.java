package it.polimi.ingsw.am01.network;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public interface Server {
    Connection<S2CNetworkMessage, C2SNetworkMessage> accept() throws OpenConnectionNetworkException;
}
