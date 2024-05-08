package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public class ServerRMIConnection extends BaseRMIConnection<S2CNetworkMessage, C2SNetworkMessage> {
    public ServerRMIConnection(Sender<S2CNetworkMessage> sender, ReceiverImpl<C2SNetworkMessage> receiver) {
        super(sender, receiver);
    }
}
