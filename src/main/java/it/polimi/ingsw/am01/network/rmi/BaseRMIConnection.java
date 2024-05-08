package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.AlreadyClosedException;
import it.polimi.ingsw.am01.network.CloseableConnection;
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
        implements CloseableConnection<S, R>, Receiver<R> {

    private final ExecutorService executorService;
    private final BlockingQueue<S> sendQueue;
    private final BlockingQueue<R> receiveQueue;
    private Receiver<S> remoteReceiver;
    private boolean sendClosed;
    private boolean receiveClosed;

    public BaseRMIConnection(ExecutorService executorService) throws RemoteException {
        super();
        this.executorService = executorService;
        this.sendQueue = new LinkedBlockingQueue<>();
        this.receiveQueue = new LinkedBlockingQueue<>();
        this.remoteReceiver = null;
        this.sendClosed = false;
        this.receiveClosed = false;
    }

    public synchronized void connect(Receiver<S> remoteReceiver) {
        this.remoteReceiver = remoteReceiver;
        this.executorService.submit(this::messageSenderWorker);
    }

    private void messageSenderWorker() {
        while (true) {
            S toSend;

            synchronized (this) {
                try {
                    while ((toSend = this.sendQueue.poll()) == null && !this.sendClosed) {
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    // interrupted while waiting for a new message to send
                    // close everything and stop
                    this.sendClosed = true;
                    this.receiveClosed = true;
                    return;
                }
            }

            try {
                // toSend = null <==> queue is empty && sendClosed = true,
                // which means we are not going to send any more messages:
                if (toSend == null) {
                    // notify the remote side that we won't send anything more and stop
                    this.remoteReceiver.end();
                    return;
                }

                this.remoteReceiver.transmit(toSend);
            } catch (AlreadyClosedException e) {
                // the remote side has already been closed
                // stop sending stuff
                this.sendClosed = true;
                this.receiveClosed = true;
                return;
            } catch (RemoteException e) {
                // TODO: better logging
                e.printStackTrace();

                // something went wrong while sending, close everything and stop
                this.sendClosed = true;
                this.receiveClosed = true;
                return;
            }
        }
    }

    @Override
    public synchronized void send(S message) throws SendNetworkException {
        // this should never actually happen
        // because the RMIServer never gives out unconnected Connections from its accept() method
        if (this.sendClosed) {
            throw new SendNetworkException("Cannot send message. Remote side is not accepting messages anymore.");
        }

        this.sendQueue.add(message);
        this.notifyAll();
    }

    @Override
    public R receive() throws ReceiveNetworkException {
        R received;
        synchronized (this) {
            // this should never actually happen
            // because the RMIServer never gives out unconnected Connections from its accept() method
            if (this.remoteReceiver == null) {
                throw new ReceiveNetworkException("Remote side is not connected yet.");
            }

            try {
                while ((received = this.receiveQueue.poll()) == null && !this.receiveClosed) {
                    this.wait();
                }
            } catch (InterruptedException e) {
                throw new ReceiveNetworkException(e);
            }
        }

        // TODO: add a way for the called to find this out before calling (and getting an exception thrown its way)
        if (received == null) {
            throw new ReceiveNetworkException("Connection is already closed.");
        }

        return received;
    }

    @Override
    public synchronized void close() {
        this.sendClosed = true;
        this.notifyAll();
    }

    @Override
    public synchronized void transmit(R message) throws AlreadyClosedException {
        if (this.receiveClosed) {
            throw new AlreadyClosedException();
        }

        receiveQueue.add(message);
        this.notifyAll();
    }

    @Override
    public synchronized void end() {
        // the remote side is signaling to us that it will no longer use this Receiver, so we can close out receive
        this.receiveClosed = true;
        // at this point, we can also close our input (if it wasn't already closed)
        this.sendClosed = true;
        this.notifyAll();
    }
}
