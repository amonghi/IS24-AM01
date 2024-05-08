package it.polimi.ingsw.am01.network.rmi.client;

import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.rmi.BaseRMIConnection;
import it.polimi.ingsw.am01.network.rmi.Receiver;
import it.polimi.ingsw.am01.network.rmi.Sender;
import it.polimi.ingsw.am01.network.rmi.server.RMIServer;
import it.polimi.ingsw.am01.network.rmi.server.RMIServerRemoteInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMIConnection extends BaseRMIConnection<C2SNetworkMessage, S2CNetworkMessage> {
    private ClientRMIConnection() throws RemoteException {
        super();
    }

    public static ClientRMIConnection connect(String host, int port) throws OpenConnectionNetworkException {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            RMIServerRemoteInterface remote = (RMIServerRemoteInterface) registry.lookup(RMIServer.SERVICE_NAME);

            ClientRMIConnection connection = new ClientRMIConnection();
            Receiver<C2SNetworkMessage> serverReceiver = remote.swapReceivers(connection);
            Sender<C2SNetworkMessage> serverSender = new Sender<>(serverReceiver);

            // FIXME: is this the best place to create this thread?
            new Thread(serverSender).start();

            connection.connect(serverSender);
            return connection;
        } catch (RemoteException | NotBoundException e) {
            throw new OpenConnectionNetworkException(e);
        }
    }
}
