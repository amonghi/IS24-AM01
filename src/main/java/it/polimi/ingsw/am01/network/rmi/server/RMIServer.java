package it.polimi.ingsw.am01.network.rmi.server;

import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.Server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer implements Server {

    public static final String SERVICE_NAME = "CodexNaturalis";

    private final RMIServerRemoteInterfaceImpl remoteImpl;

    public RMIServer(int port) throws RemoteException, AlreadyBoundException {
        this.remoteImpl = new RMIServerRemoteInterfaceImpl();

        // TODO: how do I bind to a specific network interface?
        Registry registry = LocateRegistry.createRegistry(port);
        registry.bind(SERVICE_NAME, this.remoteImpl);
    }

    @Override
    public Connection<S2CNetworkMessage, C2SNetworkMessage> accept() throws OpenConnectionNetworkException {
        try {
            return this.remoteImpl.takeConnection();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OpenConnectionNetworkException(e);
        }
    }
}
