package it.polimi.ingsw.am01.network;

/**
 * Represent a generic exception that can occur while using the network.
 */
public class NetworkException extends Exception {
    public NetworkException() {
    }

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkException(Throwable cause) {
        super(cause);
    }
}
