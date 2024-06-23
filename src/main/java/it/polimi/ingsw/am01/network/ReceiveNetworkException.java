package it.polimi.ingsw.am01.network;

/**
 * Exception thrown when an error occurs while receiving a message.
 */
public class ReceiveNetworkException extends NetworkException {
    public ReceiveNetworkException(Throwable cause) {
        super(cause);
    }

    public ReceiveNetworkException(String connectionIsNotOpen) {
        super(connectionIsNotOpen);
    }
}
