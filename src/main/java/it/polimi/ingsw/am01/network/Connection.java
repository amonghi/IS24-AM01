package it.polimi.ingsw.am01.network;

import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.network.message.NetworkMessage;

public interface Connection<I extends NetworkMessage, O extends NetworkMessage> extends EventEmitter<O> {
    void send(I message);

    O receive();
}
