package it.polimi.ingsw.am01.network;

import it.polimi.ingsw.am01.network.message.NetworkMessage;

public interface Connection<S extends NetworkMessage, R extends NetworkMessage> {
    void send(S message) throws SendNetworkException;

    R receive() throws ReceiveNetworkException;

    /**
     * Closes this connection.
     * <p>
     * Once a {@link Connection} has been closed:
     *
     * <ul>
     *     <li>it cannot be reopened</li>
     *     <li>{@link #send(NetworkMessage)} can no longer be called</li>
     *     <li>any messages that have been previously received, can still be retrieved by calling {@link #receive()}</li>
     * </ul>
     */
    void close() throws CloseNetworkException;
}
