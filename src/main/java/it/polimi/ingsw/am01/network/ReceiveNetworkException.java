package it.polimi.ingsw.am01.network;

public class ReceiveNetworkException extends NetworkException {
    public ReceiveNetworkException(Throwable cause) {
        super(cause);
    }

    public ReceiveNetworkException(String connectionIsNotOpen) {
        super(connectionIsNotOpen);
    }
}
