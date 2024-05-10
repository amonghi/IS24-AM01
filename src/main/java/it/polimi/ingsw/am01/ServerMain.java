package it.polimi.ingsw.am01;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.controller.VirtualView;
import it.polimi.ingsw.am01.model.game.GameManager;
import it.polimi.ingsw.am01.model.player.PlayerManager;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.rmi.server.RMIServer;
import it.polimi.ingsw.am01.network.Server;
import it.polimi.ingsw.am01.network.tcp.server.TCPServer;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.rmi.AlreadyBoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    private static final int TCP_PORT = 8888;
    private static final int RMI_PORT = 7777;
    private static final String HOSTNAME = "0.0.0.0";

    public static void main(String[] args) throws IOException, AlreadyBoundException {
        Path dataPath = Path.of("./data");
        PlayerManager playerManager = new PlayerManager();
        GameManager gameManager = new GameManager(dataPath);
        Controller controller = new Controller(playerManager, gameManager);
        ExecutorService executorService = Executors.newCachedThreadPool();

        TCPServer tcpServer = new TCPServer(InetAddress.getByName(HOSTNAME), TCP_PORT);
        new Thread(() -> acceptConnections(tcpServer, executorService, controller, gameManager, playerManager)).start();

        RMIServer rmiServer = new RMIServer(RMI_PORT);
        new Thread(() -> acceptConnections(rmiServer, executorService, controller, gameManager, playerManager)).start();

        System.out.println("Server started.");
    }

    private static void acceptConnections(Server server,
                                          ExecutorService executorService,
                                          Controller controller,
                                          GameManager gameManager,
                                          PlayerManager playerManager) {
        while (true) {
            try {
                Connection<S2CNetworkMessage, C2SNetworkMessage> connection = server.accept();
                VirtualView virtualView = new VirtualView(controller, connection, gameManager, playerManager);
                executorService.submit(virtualView);
            } catch (OpenConnectionNetworkException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
