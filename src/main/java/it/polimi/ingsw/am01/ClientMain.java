package it.polimi.ingsw.am01;

import it.polimi.ingsw.am01.network.client.ClientTCPConnection;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.c2s.AuthenticateC2S;

import java.io.IOException;
import java.net.InetAddress;

public class ClientMain {
    private static final int TCP_PORT = 8888;
    private static final String HOSTNAME = "0.0.0.0";

    public static void main(String[] args) throws IOException {
        ClientTCPConnection connection = new ClientTCPConnection(InetAddress.getByName(HOSTNAME), TCP_PORT);

        connection.send(new AuthenticateC2S("Mauro"));
        S2CNetworkMessage r1 = connection.receive();
        System.out.println(r1);

        connection.send(new AuthenticateC2S("Mauro"));
        S2CNetworkMessage r2 = connection.receive();
        System.out.println(r2);
    }
}
