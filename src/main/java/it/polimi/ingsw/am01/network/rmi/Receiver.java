package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.message.NetworkMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Receiver<M extends NetworkMessage> extends Remote {
    void receive(M message) throws RemoteException;
}
