package it.polimi.ingsw.am01.network.tcp.server;

import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.Server;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Implementation of {@link Server} that uses TCP.
 */
public class TCPServer implements Server {

    private final ServerSocket serverSocket;

    /**
     * Creates a new {@link TCPServer}.
     *
     * @param addr the address to bind to
     * @param port the port to bind to
     * @throws IOException if an error occurs while creating the server
     */
    public TCPServer(InetAddress addr, int port) throws IOException {
        serverSocket = new ServerSocket(port, 50, addr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection<S2CNetworkMessage, C2SNetworkMessage> accept() throws OpenConnectionNetworkException {
        try {
            Socket socket = serverSocket.accept();
            return new ServerTCPConnection(socket);
        } catch (IOException e) {
            throw new OpenConnectionNetworkException(e);
        }
    }
}
