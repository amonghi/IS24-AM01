package it.polimi.ingsw.am01.network.rmi.server;

import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.rmi.Receiver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerRemoteInterface extends Remote {
    Receiver<C2SNetworkMessage> swapReceivers(Receiver<S2CNetworkMessage> clientReceiver) throws RemoteException, OpenConnectionNetworkException;
}
