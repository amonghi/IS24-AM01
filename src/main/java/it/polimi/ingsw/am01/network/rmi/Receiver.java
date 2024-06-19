package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.message.NetworkMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Receiver<M extends NetworkMessage> extends Remote {
    void transmit(M message) throws RemoteException, AlreadyClosedException;

    /**
     * Signals to the {@link Receiver} that it is no longer going to be used (because the connection has been closed).
     * After calling this method, {@link #transmit(NetworkMessage)} can no longer be called.
     */
    void end() throws RemoteException;
}
