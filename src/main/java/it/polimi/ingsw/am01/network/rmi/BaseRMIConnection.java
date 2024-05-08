package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.ReceiveNetworkException;
import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.NetworkMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class BaseRMIConnection<S extends NetworkMessage, R extends NetworkMessage>
        extends UnicastRemoteObject
        implements Connection<S, R>, Receiver<R> {

    private final BlockingQueue<R> receiveQueue;
    private Sender<S> sender;
    private RMIConnectionStatus connectionStatus;

    public BaseRMIConnection() throws RemoteException {
        super();
        this.receiveQueue = new LinkedBlockingQueue<>();
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
            return this.receiveQueue.take();
        } catch (InterruptedException e) {
            throw new ReceiveNetworkException(e);
        }
    }

    @Override
    public void transmit(R message) throws RemoteException, SendNetworkException {
        try {
            receiveQueue.put(message);
        } catch (InterruptedException e) {
            // we throw a SendNetworkException inside a receive()
            // because this method is called remotely to send a message
            throw new SendNetworkException(e);
        }
    }

    private enum RMIConnectionStatus {
        DISCONNECTED,
        OPEN,
    }
}
