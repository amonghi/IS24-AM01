package it.polimi.ingsw.am01.network.rmi;

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

    public void send(M message) {
        this.sendQueue.add(message);
    }

    @Override
    public void run() {
        while (true) {
            try {
                M message = this.sendQueue.take();
                this.receiver.receive(message);
            } catch (InterruptedException | RemoteException e) {
                // TODO: handle exceptions
                throw new RuntimeException(e);
            }
        }
    }
}
