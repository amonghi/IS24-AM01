package it.polimi.ingsw.am01.network.tcp.client;

import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.tcp.BaseTCPConnection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * TCP connection on the client side.
 */
public class ClientTCPConnection extends BaseTCPConnection<C2SNetworkMessage, S2CNetworkMessage> {
    private ClientTCPConnection(Socket socket) throws IOException {
        super(C2SNetworkMessage.class, S2CNetworkMessage.class, socket);

        this.socket.setTcpNoDelay(true);
    }

    /**
     * Connects to a remote TCP server.
     *
     * @param address the address of the server
     * @param port    the port of the server
     * @return the connection to the server
     * @throws OpenConnectionNetworkException if an error occurs while connecting to the server
     * @throws IOException                    if an I/O error occurs when creating the connection
     */
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
