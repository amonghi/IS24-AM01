package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.ReceiveNetworkException;
import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.NetworkMessage;

public abstract class BaseRMIConnection<S extends NetworkMessage, R extends NetworkMessage> implements Connection<S, R> {

    private final Sender<S> sender;
    private final ReceiverImpl<R> receiver;

    public BaseRMIConnection(Sender<S> sender, ReceiverImpl<R> receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public void send(S message) throws SendNetworkException {
        this.sender.send(message);
    }

    @Override
    public R receive() throws ReceiveNetworkException {
        try {
            return this.receiver.takeReceived();
        } catch (InterruptedException e) {
            throw new ReceiveNetworkException(e);
        }
    }
}
