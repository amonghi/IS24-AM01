package it.polimi.ingsw.am01.network;

/**
 * Exception thrown when an error occurs while sending a message.
 */
public class SendNetworkException extends NetworkException {
    public SendNetworkException(Throwable cause) {
        super(cause);
    }

    public SendNetworkException(String s) {
        super(s);
    }
}
