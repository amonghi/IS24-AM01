package it.polimi.ingsw.am01.network.rmi.server;

import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.rmi.Receiver;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface used to open a connection to the server through RMI.
 */
public interface RMIServerRemoteInterface extends Remote {

    /**
     * By calling this method the client and the server can swap {@link Receiver}s,
     * allowing them to send messages to each other.
     *
     * @param clientReceiver the {@link Receiver} that the server will use to send messages to the client
     * @return the {@link Receiver} that the client will use to send messages to the server
     * @throws RemoteException                if a communication error occurs
     * @throws OpenConnectionNetworkException if the connection has already been closed
     */
    Receiver<C2SNetworkMessage> swapReceivers(Receiver<S2CNetworkMessage> clientReceiver) throws RemoteException, OpenConnectionNetworkException;
}
