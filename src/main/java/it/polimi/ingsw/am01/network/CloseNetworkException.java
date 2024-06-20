package it.polimi.ingsw.am01.network;

import java.io.IOException;

/**
 * Exception thrown when an error occurs while closing a connection.
 */
public class CloseNetworkException extends NetworkException {
    public CloseNetworkException(IOException e) {
    }
}
