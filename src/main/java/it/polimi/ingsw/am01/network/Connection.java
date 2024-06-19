package it.polimi.ingsw.am01.network;

import it.polimi.ingsw.am01.network.message.NetworkMessage;

/**
 * A connection to a remote host.
 * Could be a connection from a client to a server or vice versa.
 * <p>
 * A connection can be used to send and receive messages.
 * Sending a message never blocks, while receiving a message can block.
 * Messages are delivered in the order they were sent.
 *
 * @param <S> the type of messages that can be sent through this connection.
 * @param <R> the type of messages that can be received through this connection.
 */
public interface Connection<S extends NetworkMessage, R extends NetworkMessage> {
    /**
     * Sends a message through this connection.
     *
     * @param message the message to send.
     * @throws SendNetworkException if an error occurs while sending the message.
     */
    void send(S message) throws SendNetworkException;

    /**
     * Retrieves the next message from the connection.
     * If no message is available, this method blocks until a message is received.
     *
     * @return the next message.
     * @throws ReceiveNetworkException if an error occurs while receiving the message.
     */
    R receive() throws ReceiveNetworkException;

    /**
     * Closes this connection.
     * <p>
     * Once a {@link Connection} has been closed:
     * <ul>
     *     <li>it cannot be reopened</li>
     *     <li>{@link #send(NetworkMessage)} can no longer be called</li>
     *     <li>any messages that have been previously received, can still be retrieved by calling {@link #receive()}</li>
     * </ul>
     */
    void close() throws CloseNetworkException;
}
