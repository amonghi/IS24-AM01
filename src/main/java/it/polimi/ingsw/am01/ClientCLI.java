package it.polimi.ingsw.am01;

import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.GameAssets;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.ReceiveNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.c2s.*;
import it.polimi.ingsw.am01.network.message.s2c.UpdatePlayAreaS2C;
import it.polimi.ingsw.am01.network.message.s2c.UpdatePlayerListS2C;
import it.polimi.ingsw.am01.network.rmi.client.ClientRMIConnection;
import it.polimi.ingsw.am01.network.tcp.client.ClientTCPConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientCLI {
    private static final int TCP_PORT = 8888;
    private static final int RMI_PORT = 7777;
    private static final String HOSTNAME = "0.0.0.0";

    private static final Map<String, String> commandParameters = Map.ofEntries(
            Map.entry("auth", "<name>"),
            Map.entry("create_game", "<maxPlayers>"),
            Map.entry("draw_from_deck", "<resource|golden>"),
            Map.entry("draw_from_face_up", "<cardId>"),
            Map.entry("join_game", "<gameId>"),
            Map.entry("send_direct_msg", "<recipient> \n <content>"),
            Map.entry("send_broadcast_msg", "\n <content>"),
            Map.entry("place_card", "<cardId> <front|back> <i> <j>"),
            Map.entry("select_color", "<red|blue|green|yellow>"),
            Map.entry("select_obj", "<objectiveId>"),
            Map.entry("select_starting_card", "<front|back>"),
            Map.entry("start_game", ""),
            Map.entry("get_area_of", " <playerName>"),
            Map.entry("card_info", " <cardId>"),
            Map.entry("obj_info", " <objectiveId>"),
            Map.entry("get_scores", ""),
            Map.entry("close_connection", "")
    );

    public static boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    public static void main(String[] args) throws OpenConnectionNetworkException, IOException {
        String clientType = args[0].toLowerCase();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Map<String, List<Placement>> placements = new HashMap<>();
        Map<String, Integer> scores = new HashMap<>();

        ExecutorService executorService = Executors.newCachedThreadPool();


        Connection<C2SNetworkMessage, S2CNetworkMessage> connection = switch (clientType) {
            case "tcp" -> ClientTCPConnection.connect(InetAddress.getByName(HOSTNAME), TCP_PORT);
            case "rmi" -> ClientRMIConnection.connect(executorService, HOSTNAME, RMI_PORT);
            default -> throw new IllegalArgumentException("Unknown server type: " + clientType);
        };

        executorService.submit(() -> {
            String[] userInput;
            while (true) {
                userInput = reader.readLine().split(" ");
                switch (userInput[0]) {
                    case "auth" -> {
                        if (userInput.length != 2) {
                            wrongParameters(userInput[0]);
                        } else {
                            connection.send(new AuthenticateC2S(
                                    userInput[1]
                            ));
                        }
                    }
                    case "create_game" -> {
                        if (userInput.length != 2 || !isNumeric(userInput[1])) {
                            wrongParameters(userInput[0]);
                        } else {
                            connection.send(new CreateGameAndJoinC2S(
                                    Integer.parseInt(userInput[1]))
                            );
                        }
                    }
                    case "draw_from_deck" -> {
                        if (userInput.length != 2 || (!userInput[1].equals("golden") && !userInput[1].equals("resource"))) {
                            wrongParameters(userInput[0]);
                        } else {
                            connection.send(new DrawCardFromDeckC2S(
                                    switch (userInput[1]) {
                                        case "golden" -> DeckLocation.GOLDEN_CARD_DECK;
                                        case "resource" -> DeckLocation.RESOURCE_CARD_DECK;
                                        default -> throw new IllegalStateException("Unexpected value: " + userInput[1]);
                                    }
                            ));
                        }
                    }
                    case "draw_from_face_up" -> {
                        if (userInput.length != 2 || !isNumeric(userInput[1])) {
                            wrongParameters(userInput[0]);
                        } else {
                            connection.send(new DrawCardFromFaceUpCardsC2S(
                                    Integer.parseInt(userInput[1])
                            ));
                        }
                    }
                    case "join_game" -> {
                        if (userInput.length != 2 || !isNumeric(userInput[1])) {
                            wrongParameters(userInput[0]);
                        } else {
                            connection.send(new JoinGameC2S(
                                    Integer.parseInt(userInput[1])
                            ));
                        }
                    }
                    case "send_direct_msg" -> {
                        if (userInput.length != 2) {
                            wrongParameters(userInput[0]);
                        } else {
                            String msg = reader.readLine();
                            connection.send(new SendDirectMessageC2S(
                                    userInput[1],
                                    msg
                            ));
                        }
                    }
                    case "send_broadcast_msg" -> {
                        if (userInput.length != 1) {
                            wrongParameters(userInput[0]);
                        } else {
                            String msg = reader.readLine();
                            connection.send(new SendBroadcastMessageC2S(
                                    msg
                            ));
                        }
                    }
                    case "place_card" -> {
                        if (userInput.length != 5 || (!userInput[2].equals("front") && !userInput[2].equals("back")) || !isNumeric(userInput[1]) || !isNumeric(userInput[3]) || !isNumeric(userInput[4])) {
                            wrongParameters(userInput[0]);
                        } else {
                            connection.send(new PlaceCardC2S(
                                    Integer.parseInt(userInput[1]),
                                    switch (userInput[2]) {
                                        case "front" -> Side.FRONT;
                                        case "back" -> Side.BACK;
                                        default -> throw new IllegalStateException("Unexpected value: " + userInput[2]);
                                    },
                                    Integer.parseInt(userInput[3]),
                                    Integer.parseInt(userInput[4])
                            ));
                        }
                    }
                    case "select_color" -> {
                        if (userInput.length != 2 || !userInput[1].equals("red") && !userInput[1].equals("blue") && !userInput[1].equals("yellow") && !userInput[1].equals("green")) {
                            wrongParameters(userInput[0]);
                        } else {
                            connection.send(new SelectColorC2S(
                                    switch (userInput[1]) {
                                        case "red" -> PlayerColor.RED;
                                        case "blue" -> PlayerColor.BLUE;
                                        case "yellow" -> PlayerColor.YELLOW;
                                        case "green" -> PlayerColor.GREEN;
                                        default -> throw new IllegalStateException("Unexpected value: " + userInput[1]);
                                    }
                            ));
                        }
                    }
                    case "select_obj" -> {
                        if (userInput.length != 2 || !isNumeric(userInput[1])) {
                            wrongParameters(userInput[0]);
                        } else {
                            connection.send(new SelectSecretObjectiveC2S(
                                    Integer.parseInt(userInput[1])
                            ));
                        }
                    }
                    case "select_starting_card" -> {
                        if (userInput.length != 2 || !userInput[1].equals("front") && !userInput[1].equals("back")) {
                            wrongParameters(userInput[0]);
                        } else {
                            connection.send(new SelectStartingCardSideC2S(
                                    switch (userInput[1]) {
                                        case "front" -> Side.FRONT;
                                        case "back" -> Side.BACK;
                                        default -> throw new IllegalStateException("Unexpected value: " + userInput[1]);
                                    }
                            ));
                        }
                    }
                    case "start_game" -> {
                        if (userInput.length != 1) {
                            wrongParameters(userInput[0]);
                        } else {
                            connection.send(new StartGameC2S());
                        }
                    }
                    case "close_connection" -> {
                        if (userInput.length != 1) {
                            wrongParameters(userInput[0]);
                        } else {
                            connection.close();
                            executorService.shutdown();
                            System.exit(0);
                        }
                    }
                    case "get_area_of" -> {
                        if (userInput.length != 2) {
                            wrongParameters(userInput[0]);
                        } else {
                            System.out.println(placements.get(userInput[1]));
                        }
                    }
                    case "card_info" -> {
                        if (userInput.length != 2 || !isNumeric(userInput[1])) {
                            wrongParameters(userInput[0]);
                        } else {
                            GameAssets.getInstance().getCardById(Integer.parseInt(userInput[1])).ifPresent(System.out::println);
                        }
                    }
                    case "obj_info" -> {
                        if (userInput.length != 2 || !isNumeric(userInput[1])) {
                            wrongParameters(userInput[0]);
                        } else {
                            GameAssets.getInstance().getObjectiveById(Integer.parseInt(userInput[1])).ifPresent(System.out::println);
                        }
                    }
                    case "get_scores" -> {
                        if (userInput.length != 1) {
                            wrongParameters(userInput[0]);
                        } else {
                            System.out.println(scores);
                        }
                    }
                    case "help" ->
                            commandParameters.forEach((key, value) -> System.out.println("- " + key + " " + value));
                    default ->
                            System.out.println("Invalid command: " + userInput[0] + ". Type 'help' for more information.");
                }
            }
        });

        executorService.submit(() -> {
            while (true) {
                try {
                    S2CNetworkMessage message = connection.receive();
                    if(!message.getId().equals("PING"))
                        System.out.println(message);
                    switch (message.getId()) {
                        case "GAME_FINISHED":
                            placements.clear();
                            scores.clear();
                            break;

                        case "UPDATE_PLAY_AREA":
                            UpdatePlayAreaS2C updatePlayAreaS2C = (UpdatePlayAreaS2C) message;
                            placements.get(updatePlayAreaS2C.playerName()).add(new Placement(
                                    updatePlayAreaS2C.i(),
                                    updatePlayAreaS2C.j(),
                                    updatePlayAreaS2C.cardId(),
                                    updatePlayAreaS2C.side(),
                                    updatePlayAreaS2C.seq(),
                                    updatePlayAreaS2C.points()
                            ));

                            scores.put(updatePlayAreaS2C.playerName(),
                                    scores.get(updatePlayAreaS2C.playerName()) + updatePlayAreaS2C.points()
                            );

                            break;
                        case "UPDATE_PLAYER_LIST":
                            UpdatePlayerListS2C updatePlayerListS2C = (UpdatePlayerListS2C) message;
                            updatePlayerListS2C.playerList().forEach(player -> {
                                placements.put(player, new ArrayList<>());
                                scores.put(player, 0);
                            });

                            break;
                    }

                } catch (ReceiveNetworkException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static void wrongParameters(String command) {
        System.out.println("Incorrect command usage: " + command + " " + commandParameters.get(command));
    }

    private record Placement(int i, int j, int cardId, Side side, int seq, int points) {
    }

}
