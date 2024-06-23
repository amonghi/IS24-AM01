package it.polimi.ingsw.am01.network;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * Server interface.
 */
public interface Server {
    /**
     * Accepts an incoming connection from a client.
     * <p>
     * This method blocks until a connection is established.
     *
     * @return the connection.
     * @throws OpenConnectionNetworkException if an error occurs while opening the connection.
     */
    Connection<S2CNetworkMessage, C2SNetworkMessage> accept() throws OpenConnectionNetworkException;
}
