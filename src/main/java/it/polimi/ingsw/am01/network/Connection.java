package it.polimi.ingsw.am01.network;

import it.polimi.ingsw.am01.network.message.NetworkMessage;

public interface Connection<S extends NetworkMessage, R extends NetworkMessage> {
    void send(S message) throws SendNetworkException;

    R receive() throws ReceiveNetworkException;
}
