package it.polimi.ingsw.am01.network;

public class OpenConnectionNetworkException extends NetworkException {
    public OpenConnectionNetworkException(Throwable cause) {
        super("Could not open connection", cause);
    }
}
