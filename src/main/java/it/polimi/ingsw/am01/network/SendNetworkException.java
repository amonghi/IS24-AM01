package it.polimi.ingsw.am01.network;

public class SendNetworkException extends NetworkException {
    public SendNetworkException(Throwable cause) {
        super(cause);
    }

    public SendNetworkException(String s) {
        super(s);
    }
}
