package it.polimi.ingsw.am01.network.tcp.client;

import it.polimi.ingsw.am01.network.tcp.BaseTCPConnection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTCPConnection extends BaseTCPConnection<C2SNetworkMessage, S2CNetworkMessage> {
    public ClientTCPConnection(InetAddress address, int port) throws IOException {
        super(C2SNetworkMessage.class, S2CNetworkMessage.class, new Socket(address, port));

        this.socket.setTcpNoDelay(true);
    }
}
