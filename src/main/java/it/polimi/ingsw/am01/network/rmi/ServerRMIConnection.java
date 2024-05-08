package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.rmi.RemoteException;

public class ServerRMIConnection extends BaseRMIConnection<S2CNetworkMessage, C2SNetworkMessage> {
    public ServerRMIConnection() throws RemoteException {
        super();
    }
}
