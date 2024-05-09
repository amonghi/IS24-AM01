package it.polimi.ingsw.am01.network.rmi.client;

import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.rmi.BaseRMIConnection;
import it.polimi.ingsw.am01.network.rmi.Receiver;
import it.polimi.ingsw.am01.network.rmi.server.RMIServer;
import it.polimi.ingsw.am01.network.rmi.server.RMIServerRemoteInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;

public class ClientRMIConnection extends BaseRMIConnection<C2SNetworkMessage, S2CNetworkMessage> {
    private ClientRMIConnection(ExecutorService executorService) throws RemoteException {
        super(executorService);
    }

    public static ClientRMIConnection connect(ExecutorService executorService, String host, int port) throws OpenConnectionNetworkException {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            RMIServerRemoteInterface remote = (RMIServerRemoteInterface) registry.lookup(RMIServer.SERVICE_NAME);

            ClientRMIConnection connection = new ClientRMIConnection(executorService);
            Receiver<C2SNetworkMessage> serverReceiver = remote.swapReceivers(connection);

            connection.connect(serverReceiver);
            return connection;
        } catch (RemoteException | NotBoundException e) {
            throw new OpenConnectionNetworkException(e);
        }
    }
}
