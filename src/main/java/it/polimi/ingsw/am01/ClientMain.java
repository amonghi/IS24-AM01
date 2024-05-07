package it.polimi.ingsw.am01;

import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.ReceiveNetworkException;
import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.c2s.AuthenticateC2S;
import it.polimi.ingsw.am01.network.rmi.client.ClientRMIConnection;
import it.polimi.ingsw.am01.network.tcp.client.ClientTCPConnection;

import java.io.IOException;
import java.net.InetAddress;

public class ClientMain {
    private static final int TCP_PORT = 8888;
    private static final int RMI_PORT = 7777;
    private static final String HOSTNAME = "0.0.0.0";

    public static void main(String[] args) throws IOException, ReceiveNetworkException, OpenConnectionNetworkException, SendNetworkException {
        String clientType = "rmi".toLowerCase();

        Connection<C2SNetworkMessage, S2CNetworkMessage> connection = switch (clientType) {
            case "tcp" -> new ClientTCPConnection(InetAddress.getByName(HOSTNAME), TCP_PORT);
            case "rmi" -> ClientRMIConnection.connect("localhost", RMI_PORT);
            default -> throw new IllegalArgumentException("Unknown server type: " + clientType);
        };

        connection.send(new AuthenticateC2S("Mauro"));
        S2CNetworkMessage r1 = connection.receive();
        System.out.println(r1);

        connection.send(new AuthenticateC2S("Mauro"));
        S2CNetworkMessage r2 = connection.receive();
        System.out.println(r2);
    }
}
