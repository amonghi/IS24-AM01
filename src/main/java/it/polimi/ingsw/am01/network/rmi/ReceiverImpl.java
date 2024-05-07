package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.NetworkMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReceiverImpl<M extends NetworkMessage> extends UnicastRemoteObject implements Receiver<M> {
    private final BlockingQueue<M> receiveQueue;

    public ReceiverImpl() throws RemoteException {
        super();
        receiveQueue = new LinkedBlockingQueue<>();
    }

    public M takeReceived() throws InterruptedException {
        return receiveQueue.take();
    }

    @Override
    public void receive(M message) throws RemoteException, SendNetworkException {
        try {
            receiveQueue.put(message);
        } catch (InterruptedException e) {
            // we throw a SendNetworkException inside a receive()
            // because this method is called remotely to send a message
            throw new SendNetworkException(e);
        }
    }
}
