package it.polimi.ingsw.am01.network.rmi.server;

import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.rmi.BaseRMIConnection;
import it.polimi.ingsw.am01.network.rmi.Receiver;
import it.polimi.ingsw.am01.network.rmi.Sender;
import it.polimi.ingsw.am01.network.rmi.ServerRMIConnection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.*;

public class RMIServerRemoteInterfaceImpl extends UnicastRemoteObject implements RMIServerRemoteInterface {
    private final BlockingQueue<PendingConnection> pendingConnections;
    private final ExecutorService executorService;

    public RMIServerRemoteInterfaceImpl() throws RemoteException {
        super();
        pendingConnections = new LinkedBlockingQueue<>();
        this.executorService = Executors.newCachedThreadPool();
    }

    // takes a connection from the queue or blocks if there are none yet
    public BaseRMIConnection<S2CNetworkMessage, C2SNetworkMessage> takeConnection() throws InterruptedException {
        PendingConnection pendingConnection = pendingConnections.take();

        // unlock remote call when accepting connection
        pendingConnection.semaphore.release();

        return pendingConnection.connection;
    }

    @Override
    public Receiver<C2SNetworkMessage> swapReceivers(Receiver<S2CNetworkMessage> clientReceiver) throws RemoteException, OpenConnectionNetworkException {
        ServerRMIConnection connection = new ServerRMIConnection();

        Sender<S2CNetworkMessage> clientSender = new Sender<>(clientReceiver);
        executorService.submit(clientSender);
        connection.connect(clientSender);

        PendingConnection pendingConnection = new PendingConnection(connection, new Semaphore(0));
        pendingConnections.add(pendingConnection);

        // block remote call until the server accepts this connection
        try {
            pendingConnection.semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OpenConnectionNetworkException(e);
        }

        return pendingConnection.connection;
    }

    // represents an incoming connection that hasn't been accepted yet
    private record PendingConnection(BaseRMIConnection<S2CNetworkMessage, C2SNetworkMessage> connection,
                                     Semaphore semaphore) {
    }
}
