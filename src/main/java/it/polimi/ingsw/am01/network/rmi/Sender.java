package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.NetworkMessage;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Sender<M extends NetworkMessage> implements Runnable {

    private final Receiver<M> receiver;
    private final BlockingQueue<M> sendQueue;

    public Sender(Receiver<M> receiver) {
        this.receiver = receiver;
        sendQueue = new LinkedBlockingQueue<>();
    }

    public void send(M message) throws SendNetworkException {
        try {
            this.sendQueue.put(message);
        } catch (InterruptedException e) {
            throw new SendNetworkException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                M message = this.sendQueue.take();
                this.receiver.transmit(message);
            } catch (InterruptedException | SendNetworkException | RemoteException e) {
                // TODO: handle exceptions
                throw new RuntimeException(e);
            }
        }
    }
}
