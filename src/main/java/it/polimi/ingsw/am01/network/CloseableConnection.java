package it.polimi.ingsw.am01.network;

import it.polimi.ingsw.am01.network.message.NetworkMessage;

// this is a temporary interface that is needed while i implement the close() method for all the Connection types
// once all Connection types will implement this, it will be merged back into Connection
// TODO: delete this interface
public interface CloseableConnection<S extends NetworkMessage, R extends NetworkMessage> extends Connection<S, R> {
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
