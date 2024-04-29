package it.polimi.ingsw.am01.network.client;

import it.polimi.ingsw.am01.BaseTCPConnection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.io.IOException;
import java.net.Socket;

public class ClientTCPConnection extends BaseTCPConnection<C2SNetworkMessage, S2CNetworkMessage> {

    public ClientTCPConnection(Socket socket) throws IOException {
        super(C2SNetworkMessage.class, S2CNetworkMessage.class, socket);
    }
}
