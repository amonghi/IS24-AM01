package it.polimi.ingsw.am01.client.gui;

import it.polimi.ingsw.am01.network.CloseNetworkException;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.rmi.client.ClientRMIConnection;
import it.polimi.ingsw.am01.network.tcp.client.ClientTCPConnection;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GUIClient extends Application {

    private static final int TCP_PORT = 8888;
    private static final int RMI_PORT = 7777;
    private static final String HOSTNAME = "0.0.0.0";
    private static Connection<C2SNetworkMessage, S2CNetworkMessage> connection;
    private static ExecutorService executorService;

    public static void main(String[] args) throws IOException, OpenConnectionNetworkException {
        String clientType = args[0].toLowerCase();

        executorService = Executors.newCachedThreadPool();

        connection = switch (clientType) {
            case "tcp" -> ClientTCPConnection.connect(InetAddress.getByName(HOSTNAME), TCP_PORT);
            case "rmi" -> ClientRMIConnection.connect(executorService, HOSTNAME, RMI_PORT);
            default -> throw new IllegalArgumentException("Unknown server type: " + clientType);
        };

        launch();
    }

    public static void closeClient() {
        executorService.shutdown();
        try {
            connection.close();
        } catch (CloseNetworkException e) {
            throw new RuntimeException(e); //TODO: handle
        }
    }

    @Override
    public void start(Stage stage) {
        new GUIView(connection, stage);
    }
}
