package it.polimi.ingsw.am01.network.rmi.server;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.rmi.BaseRMIConnection;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;

/**
 * RMI connection on the server side.
 */
public class ServerRMIConnection extends BaseRMIConnection<S2CNetworkMessage, C2SNetworkMessage> {
    public ServerRMIConnection(ExecutorService executorService) throws RemoteException {
        super(executorService);
    }
}
