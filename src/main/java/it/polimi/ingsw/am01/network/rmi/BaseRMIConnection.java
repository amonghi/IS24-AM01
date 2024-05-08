package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.ReceiveNetworkException;
import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.NetworkMessage;

public abstract class BaseRMIConnection<S extends NetworkMessage, R extends NetworkMessage> implements Connection<S, R> {

    private final ReceiverImpl<R> receiver;
    private Sender<S> sender;
    private RMIConnectionStatus connectionStatus;

    public BaseRMIConnection(ReceiverImpl<R> receiver) {
        this.receiver = receiver;
        this.sender = null;
        this.connectionStatus = RMIConnectionStatus.DISCONNECTED;
    }

    public void connect(Sender<S> sender) {
        // TODO: accept remote receiver
        this.sender = sender;
        this.connectionStatus = RMIConnectionStatus.OPEN;
    }

    @Override
    public void send(S message) throws SendNetworkException {
        // this should never actually happen
        // because the RMIServer never gives out unconnected Connections from its accept() method
        if (this.connectionStatus != RMIConnectionStatus.OPEN) {
            throw new IllegalStateException("Connection is not open");
        }

        this.sender.send(message);
    }

    @Override
    public R receive() throws ReceiveNetworkException {
        // this should never actually happen
        // because the RMIServer never gives out unconnected Connections from its accept() method
        if (this.connectionStatus != RMIConnectionStatus.OPEN) {
            throw new IllegalStateException("Connection is not open");
        }

        try {
            return this.receiver.takeReceived();
        } catch (InterruptedException e) {
            throw new ReceiveNetworkException(e);
        }
    }

    private enum RMIConnectionStatus {
        DISCONNECTED,
        OPEN,
    }
}
