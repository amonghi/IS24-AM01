package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.message.NetworkMessage;

public class ServerRMIConnection<S extends NetworkMessage, R extends NetworkMessage> extends BaseRMIConnection<S, R> {
    public ServerRMIConnection(Sender<S> sender, ReceiverImpl<R> receiver) {
        super(sender, receiver);
    }
}
