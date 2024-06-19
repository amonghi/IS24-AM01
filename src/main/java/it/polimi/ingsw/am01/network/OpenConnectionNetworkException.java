package it.polimi.ingsw.am01.network;

/**
 * Exception thrown when an error occurs while opening a connection.
 */
public class OpenConnectionNetworkException extends NetworkException {
    public OpenConnectionNetworkException(Throwable cause) {
        super("Could not open connection", cause);
    }
}
