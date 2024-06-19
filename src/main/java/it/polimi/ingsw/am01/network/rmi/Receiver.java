package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.message.NetworkMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface used to receive a message through RMI.
 *
 * @param <M> the type of the messages that can be received
 */
public interface Receiver<M extends NetworkMessage> extends Remote {

    /**
     * Transmits a message to the {@link Receiver}.
     *
     * @param message the message to transmit
     * @throws RemoteException        if a communication error occurs
     * @throws AlreadyClosedException if the connection has already been closed
     */
    void transmit(M message) throws RemoteException, AlreadyClosedException;

    /**
     * Signals to the {@link Receiver} that it is no longer going to be used (because the connection has been closed).
     * After calling this method, {@link #transmit(NetworkMessage)} can no longer be called.
     *
     * @throws RemoteException if a communication error occurs
     */
    void end() throws RemoteException;
}
