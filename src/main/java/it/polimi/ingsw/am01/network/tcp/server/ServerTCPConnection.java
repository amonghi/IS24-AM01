package it.polimi.ingsw.am01.network.tcp.server;

import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.tcp.BaseTCPConnection;

import java.io.IOException;
import java.net.Socket;

/**
 * TCP connection on the server side.
 */
public class ServerTCPConnection extends BaseTCPConnection<S2CNetworkMessage, C2SNetworkMessage> {

    public ServerTCPConnection(Socket socket) throws IOException {
        super(S2CNetworkMessage.class, C2SNetworkMessage.class, socket);
    }
}
