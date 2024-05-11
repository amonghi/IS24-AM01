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

public class TCPServer implements Server {

    private final ServerSocket serverSocket;

    public TCPServer(InetAddress addr, int port) throws IOException {
        serverSocket = new ServerSocket(port, 50, addr);
    }

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
