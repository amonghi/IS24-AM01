package it.polimi.ingsw.am01.network;

import it.polimi.ingsw.am01.network.server.NetworkException;

public class OpenConnectionNetworkException extends NetworkException {
    public OpenConnectionNetworkException(Throwable cause) {
        super("Could not open connection", cause);
    }
}
