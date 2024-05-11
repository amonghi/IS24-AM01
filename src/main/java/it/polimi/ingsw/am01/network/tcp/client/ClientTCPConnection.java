package it.polimi.ingsw.am01.network.tcp.client;

import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.tcp.BaseTCPConnection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTCPConnection extends BaseTCPConnection<C2SNetworkMessage, S2CNetworkMessage> {
    private ClientTCPConnection(Socket socket) throws IOException {
        super(C2SNetworkMessage.class, S2CNetworkMessage.class, socket);

        this.socket.setTcpNoDelay(true);
    }

    public static ClientTCPConnection connect(InetAddress address, int port) throws OpenConnectionNetworkException, IOException {
        Socket socket;
        try {
            socket = new Socket(address, port);
        } catch (IOException e) {
            throw new OpenConnectionNetworkException(e);
        }

        return new ClientTCPConnection(socket);
    }
}
