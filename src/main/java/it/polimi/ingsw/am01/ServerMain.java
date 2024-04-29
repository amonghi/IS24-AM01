package it.polimi.ingsw.am01;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.controller.VirtualView;
import it.polimi.ingsw.am01.model.game.GameManager;
import it.polimi.ingsw.am01.model.player.PlayerManager;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.server.TCPServer;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {

    private static final int TCP_PORT = 8888;
    private static final String HOSTNAME = "0.0.0.0";

    public static void main(String[] args) throws IOException {
        Path dataPath = Path.of("./data");
        PlayerManager playerManager = new PlayerManager();
        GameManager gameManager = new GameManager(dataPath);
        Controller controller = new Controller(playerManager, gameManager);
        TCPServer tcpServer = new TCPServer(InetAddress.getByName(HOSTNAME), TCP_PORT);
        ExecutorService executorService = Executors.newCachedThreadPool();

        new Thread(() -> {
            while (true) {
                try {
                    Connection<S2CNetworkMessage, C2SNetworkMessage> connection = tcpServer.accept();
                    VirtualView virtualView = new VirtualView(controller, connection);
                    executorService.submit(virtualView);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        System.out.println("Server started. Listening on " + HOSTNAME + ":" + TCP_PORT);
    }
}
