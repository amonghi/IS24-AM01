package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.ReceiveNetworkException;
import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.NetworkMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class BaseRMIConnection<S extends NetworkMessage, R extends NetworkMessage>
        extends UnicastRemoteObject
        implements Connection<S, R>, Receiver<R> {

    private final ExecutorService executorService;
    private final BlockingQueue<S> sendQueue;
    private final BlockingQueue<R> receiveQueue;
    private RMIConnectionStatus connectionStatus;
    private Receiver<S> remoteReceiver;

    public BaseRMIConnection(ExecutorService executorService) throws RemoteException {
        super();
        this.executorService = executorService;
        this.sendQueue = new LinkedBlockingQueue<>();
        this.receiveQueue = new LinkedBlockingQueue<>();
        this.connectionStatus = RMIConnectionStatus.DISCONNECTED;
    }

    public void connect(Receiver<S> remoteReceiver) {
        this.remoteReceiver = remoteReceiver;
        this.connectionStatus = RMIConnectionStatus.OPEN;
        this.executorService.submit(this::messageSenderWorker);
    }

    @Override
    public void send(S message) throws SendNetworkException {
        // this should never actually happen
        // because the RMIServer never gives out unconnected Connections from its accept() method
        if (this.connectionStatus != RMIConnectionStatus.OPEN) {
            throw new IllegalStateException("Connection is not open");
        }

        try {
            this.sendQueue.put(message);
        } catch (InterruptedException e) {
            throw new SendNetworkException(e);
        }
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

    private void messageSenderWorker() {
        while (true) {
            try {
                S message = this.sendQueue.take();
                this.remoteReceiver.transmit(message);
            } catch (InterruptedException | SendNetworkException | RemoteException e) {
                // TODO: handle exceptions
                throw new RuntimeException(e);
            }
        }
    }

    private enum RMIConnectionStatus {
        DISCONNECTED,
        OPEN,
    }
}
