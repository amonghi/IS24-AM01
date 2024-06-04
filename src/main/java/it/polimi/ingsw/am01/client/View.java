package it.polimi.ingsw.am01.client;

import it.polimi.ingsw.am01.client.gui.event.*;
import it.polimi.ingsw.am01.client.gui.model.Placement;
import it.polimi.ingsw.am01.client.gui.model.Position;
import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.eventemitter.EventEmitterImpl;
import it.polimi.ingsw.am01.eventemitter.EventListener;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.chat.MessageType;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.TurnPhase;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.network.*;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.c2s.*;
import it.polimi.ingsw.am01.network.message.s2c.*;
import it.polimi.ingsw.am01.network.rmi.client.ClientRMIConnection;
import it.polimi.ingsw.am01.network.tcp.client.ClientTCPConnection;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class View implements EventEmitter<ViewEvent> {
    private static View instance = null;

    private final ExecutorService executorService;
    private final EventEmitterImpl<ViewEvent> emitter;

    private final Set<Integer> faceUpCards;
    private final Set<Integer> hand;
    private final Map<DeckLocation, CardColor> decksColor;
    private final Map<String, SortedSet<Placement>> playAreas;
    private final List<String> playersInGame;
    private final Map<String, PlayerColor> playerColors;
    private final Map<String, Integer> scores;
    private final List<Integer> secretObjectivesId;
    private final Map<String, Boolean> connections;
    private final List<Message> messages;
    private Connection<C2SNetworkMessage, S2CNetworkMessage> connection;
    private Map<Integer, UpdateGameListS2C.GameStat> games;
    private String playerName;
    private int gameId;
    private int startingCardId;
    private ClientState state;
    private GameStatus gameStatus;
    private TurnPhase turnPhase;
    private String currentPlayer;
    private int secretObjectiveChoiceId;
    private List<Integer> commonObjectivesId;

    private View() {
        this.emitter = new EventEmitterImpl<>();
        this.executorService = Executors.newCachedThreadPool();

        this.games = new HashMap<>();
        this.playAreas = new HashMap<>();
        this.playersInGame = new ArrayList<>();
        this.currentPlayer = null;
        this.turnPhase = null;
        this.gameStatus = null;
        this.secretObjectivesId = new ArrayList<>();
        this.commonObjectivesId = new ArrayList<>();
        this.hand = new HashSet<>();
        this.faceUpCards = new HashSet<>();
        this.decksColor = new HashMap<>();
        this.scores = new HashMap<>();
        this.playerColors = new HashMap<>();
        this.connections = new HashMap<>();
        this.messages = new ArrayList<>();
        this.state = ClientState.NOT_CONNECTED;
    }

    public static View getInstance() {
        if (instance == null) {
            instance = new View();
        }

        return instance;
    }

    public void connect(ConnectionType connectionType, String hostname, int port) {
        try {
            this.connection = switch (connectionType) {
                case ConnectionType.TCP -> ClientTCPConnection.connect(InetAddress.getByName(hostname), port);
                case ConnectionType.RMI -> ClientRMIConnection.connect(executorService, hostname, port);
            };

            new Thread(() -> {
                while (true) {
                    try {
                        S2CNetworkMessage message = connection.receive();
                        Platform.runLater(() -> {
                            switch (message) {
                                case SetPlayerNameS2C m -> handleMessage(m);
                                case NameAlreadyTakenS2C m -> handleMessage(m);
                                case UpdateGameListS2C m -> handleMessage(m);
                                case SetPlayablePositionsS2C m -> handleMessage(m);
                                case UpdatePlayAreaS2C m -> handleMessage(m);
                                case InvalidPlacementS2C m -> handleMessage(m);
                                case GameJoinedS2C m -> handleMessage(m);
                                case UpdatePlayerListS2C m -> handleMessage(m);
                                case SetStartingCardS2C m -> handleMessage(m);
                                case UpdateGameStatusS2C m -> handleMessage(m);
                                case UpdatePlayerColorS2C m -> handleMessage(m);
                                case UpdateGameStatusAndSetupObjectiveS2C m -> handleMessage(m);
                                case UpdateObjectiveSelectedS2C m -> handleMessage(m);
                                case UpdatePlayAreaAfterUndoS2C m -> handleMessage(m);
                                case SetBoardAndHandS2C m -> handleMessage(m);
                                case UpdatePlayerHandS2C m -> handleMessage(m);
                                case UpdateDeckStatusS2C m -> handleMessage(m);
                                case UpdateGameStatusAndTurnS2C m -> handleMessage(m);
                                case UpdateFaceUpCardsS2C m -> handleMessage(m);
                                case PlayerDisconnectedS2C m -> handleMessage(m);
                                case PlayerReconnectedS2C m -> handleMessage(m);
                                case SetupAfterReconnectionS2C m -> handleMessage(m);
                                case SetGamePauseS2C m -> handleMessage(m);
                                case GameResumedS2C m -> handleMessage(m);
                                case GameFinishedS2C m -> handleMessage(m);
                                case KickedFromGameS2C m -> handleMessage(m);
                                case NewMessageS2C m -> handleMessage(m);
                                case BroadcastMessageSentS2C m -> handleMessage(m);
                                case DirectMessageSentS2C m -> handleMessage(m);
                                case PingS2C m -> {
                                }
                                default ->
                                        throw new IllegalStateException("Unexpected value: " + message); //TODO: manage
                            }
                        });
                    } catch (ReceiveNetworkException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();

            state = ClientState.NOT_AUTHENTICATED;
            emitter.emit(new StateChangedEvent(state, gameStatus));
        } catch (OpenConnectionNetworkException | IOException | IllegalArgumentException e) {
            state = ClientState.NOT_CONNECTED;
            emitter.emit(new StateChangedEvent(state, gameStatus));
            emitter.emit(new ConnectionLostEvent(e.getMessage()));
        }
    }

    public void closeConnection() {
        if (connection != null) {
            executorService.shutdown();
            try {
                connection.close();
            } catch (CloseNetworkException e) {
                throw new RuntimeException(e); //TODO: handle
            }
        }
    }

    private void handleMessage(SetPlayerNameS2C message) {
        this.playerName = message.playerName();
        changeState(ClientState.AUTHENTICATED);
    }

    private void handleMessage(NameAlreadyTakenS2C message) {
        emitter.emit(new NameAlreadyTakenEvent(message.refusedName()));
    }

    private void handleMessage(UpdateGameListS2C message) {
        this.games = message.gamesStatMap();
        emitter.emit(new GameListChangedEvent(games));
    }

    private void handleMessage(SetPlayablePositionsS2C message) {
        List<Position> playablePositions = message
                .playablePositions()
                .stream()
                .map(playablePosition -> new Position(playablePosition.i(), playablePosition.j()))
                .toList();
        emitter.emit(new UpdatePlayablePositionsEvent(playablePositions));
    }

    private void handleMessage(UpdatePlayAreaS2C message) {
        if (!playAreas.containsKey(message.playerName()))
            playAreas.put(message.playerName(), new TreeSet<>());

        playAreas.get(message.playerName()).add(new Placement(message.cardId(), message.side(), new Position(message.i(), message.j()), message.seq(), message.points()));
        scores.put(message.playerName(), scores.getOrDefault(message.playerName(), 0) + message.points());

        emitter.emit(new UpdatePlayAreaEvent(
                message.playerName(),
                message.i(),
                message.j(),
                message.cardId(),
                message.side(),
                message.seq(),
                message.points()
        ));
    }

    private void handleMessage(InvalidPlacementS2C message) {
        emitter.emit(new InvalidPlacementEvent());
    }

    private void handleMessage(UpdatePlayAreaAfterUndoS2C message) {
        scores.replace(message.profile(), message.score());
        emitter.emit(new RemoveLastPlacementEvent(message.profile()));
    }

    private void handleMessage(SetBoardAndHandS2C message) {
        //Set Objectives:
        commonObjectivesId = new ArrayList<>(message.commonObjectives());

        //Set board
        faceUpCards.clear();
        faceUpCards.addAll(message.faceUpCards());
        hand.clear();
        hand.addAll(message.hand());
        decksColor.put(DeckLocation.RESOURCE_CARD_DECK, message.resourceDeckColor());
        decksColor.put(DeckLocation.GOLDEN_CARD_DECK, message.goldenDeckColor());

        // FIXME: stage.setFullScreen(true);
        changeGameStatus(GameStatus.PLAY);


        emitter.emit(new SetObjectives(commonObjectivesId, secretObjectiveChoiceId));
        emitter.emit(new SetFaceUpCardsEvent(faceUpCards.stream().toList()));
        emitter.emit(new SetDeckEvent(
                Optional.of(decksColor.get(DeckLocation.GOLDEN_CARD_DECK)),
                Optional.of(decksColor.get(DeckLocation.RESOURCE_CARD_DECK))
        ));
        emitter.emit(new SetHandEvent(hand));

        Placement starterCard = playAreas.get(playerName).getFirst();

        emitter.emit(new UpdatePlayAreaEvent(
                playerName,
                starterCard.pos().i(),
                starterCard.pos().j(),
                starterCard.id(),
                starterCard.side(),
                starterCard.seq(),
                scores.get(playerName)
        ));
        emitter.emit(new SetPlayStatusEvent(playersInGame, playerColors, scores, connections));
    }

    private void handleMessage(UpdatePlayerHandS2C message) {
        hand.clear();
        hand.addAll(message.currentHand());
        emitter.emit(new SetHandEvent(hand));
    }

    private void handleMessage(UpdateDeckStatusS2C message) {
        decksColor.replace(DeckLocation.RESOURCE_CARD_DECK, message.resourceDeckColor());
        decksColor.replace(DeckLocation.GOLDEN_CARD_DECK, message.goldenDeckColor());
        emitter.emit(new SetDeckEvent(
                Optional.of(decksColor.get(DeckLocation.GOLDEN_CARD_DECK)),
                Optional.of(decksColor.get(DeckLocation.RESOURCE_CARD_DECK))
        ));
    }

    private void handleMessage(UpdateGameStatusAndTurnS2C message) {
        this.currentPlayer = message.currentPlayerName();
        this.gameStatus = message.gameStatus();
        this.turnPhase = message.turnPhase();
        emitter.emit(new UpdateGameTurnEvent(playerName, currentPlayer, turnPhase.toString(), gameStatus.toString()));
    }

    private void handleMessage(UpdateFaceUpCardsS2C message) {
        faceUpCards.clear();
        faceUpCards.addAll(message.faceUpCards());
        emitter.emit(new SetFaceUpCardsEvent(faceUpCards.stream().toList()));
    }

    private void handleMessage(GameJoinedS2C message) {
        gameId = message.gameId();
        changeGameStatus(GameStatus.AWAITING_PLAYERS);
    }

    private void handleMessage(UpdatePlayerListS2C m) {
        emitter.emit(new PlayerListChangedEvent(m.playerList()));
        playersInGame.clear();
        playersInGame.addAll(m.playerList());
        connections.clear();
        playersInGame.forEach(p -> connections.put(p, true));
    }

    private void handleMessage(SetStartingCardS2C m) {
        startingCardId = m.startingCardId();
        changeGameStatus(GameStatus.SETUP_STARTING_CARD_SIDE);
    }

    private void handleMessage(UpdateGameStatusS2C m) {
        if (m.gameStatus().equals(GameStatus.SETUP_COLOR)) {
            changeGameStatus(GameStatus.SETUP_COLOR);
        }
    }

    private void handleMessage(UpdatePlayerColorS2C m) {
        playerColors.put(m.player(), m.color());
        emitter.emit(new UpdatePlayerColorEvent(
                m.player(),
                m.color()
        ));
    }

    private void handleMessage(UpdateGameStatusAndSetupObjectiveS2C m) {
        secretObjectivesId.addAll(
                List.of(
                        m.objectiveId1(), m.objectiveId2()
                )
        );
        changeGameStatus(GameStatus.SETUP_OBJECTIVE);
    }

    private void handleMessage(UpdateObjectiveSelectedS2C m) {
        emitter.emit(new UpdateSecretObjectiveChoiceEvent(
                m.playersHaveChosen().stream().toList()
        ));
    }

    private void handleMessage(PlayerDisconnectedS2C m) {
        if (connections.get(m.playerName()) != null) {
            connections.replace(m.playerName(), false);
        }
        emitter.emit(new SetPlayStatusEvent(playersInGame, playerColors, scores, connections));
    }

    private void handleMessage(PlayerReconnectedS2C m) {
        connections.replace(m.playerName(), true);
        emitter.emit(new SetPlayStatusEvent(playersInGame, playerColors, scores, connections));
    }

    private void handleMessage(GameFinishedS2C m) {
        changeGameStatus(GameStatus.FINISHED);
        emitter.emit(new SetFinalScoresEvent(m.finalScores()));
        clearData();
    }

    private void handleMessage(SetupAfterReconnectionS2C m) {
        currentPlayer = m.currentPlayer();
        playersInGame.addAll(m.playAreas().keySet());
        turnPhase = m.turnPhase();
        gameStatus = m.status();
        hand.addAll(m.hand());
        playerColors.putAll(m.playerColors());
        commonObjectivesId = m.commonObjectives();
        secretObjectiveChoiceId = m.secretObjective();
        decksColor.put(DeckLocation.RESOURCE_CARD_DECK, m.resourceDeckColor());
        decksColor.put(DeckLocation.GOLDEN_CARD_DECK, m.goldenDeckColor());
        faceUpCards.addAll(m.faceUpCards());
        connections.putAll(m.connections());

        for (SetupAfterReconnectionS2C.Message msg : m.chat()) {
            messages.add(new Message(
                    msg.messageType(),
                    msg.sender(),
                    msg.recipient(),
                    msg.content(),
                    msg.timestamp()
            ));
        }

        m.playAreas().forEach((player, pa) -> {
            playAreas.put(player, new TreeSet<>());
            pa.forEach(((position, cardPlacement) -> {
                playAreas.get(player).add(new Placement(
                        cardPlacement.cardId(),
                        cardPlacement.side(),
                        new Position(position.i(), position.j()),
                        cardPlacement.seq(),
                        cardPlacement.points()
                ));
            }));
        });

        playersInGame.forEach(player -> scores.put(player,
                playAreas.get(player).stream().mapToInt(Placement::points).sum()
        ));
        if (gameStatus == GameStatus.RESTORING) {
            changeGameStatus(GameStatus.RESTORING);
        } else {
            changeGameStatus(GameStatus.PLAY);
            emitter.emit(new SetObjectives(commonObjectivesId, secretObjectiveChoiceId));
            emitter.emit(new SetFaceUpCardsEvent(faceUpCards.stream().toList()));
            emitter.emit(new SetDeckEvent(
                    Optional.of(decksColor.get(DeckLocation.GOLDEN_CARD_DECK)),
                    Optional.of(decksColor.get(DeckLocation.RESOURCE_CARD_DECK))
            ));
            emitter.emit(new SetHandEvent(hand));

            emitter.emit(new SetPlayAreaEvent(playAreas.get(playerName)));
        }

        emitter.emit(new SetPlayStatusEvent(playersInGame, playerColors, scores, connections));
    }

    private void handleMessage(SetGamePauseS2C m) {
        gameStatus = m.getGameStatus();
        emitter.emit(new GamePausedEvent());
    }

    private void handleMessage(GameResumedS2C m) {
        if (gameStatus == GameStatus.RESTORING) {
            changeGameStatus(GameStatus.PLAY);

            emitter.emit(new SetObjectives(commonObjectivesId, secretObjectiveChoiceId));
            emitter.emit(new SetFaceUpCardsEvent(faceUpCards.stream().toList()));
            emitter.emit(new SetDeckEvent(
                    Optional.of(decksColor.get(DeckLocation.GOLDEN_CARD_DECK)),
                    Optional.of(decksColor.get(DeckLocation.RESOURCE_CARD_DECK))
            ));
            emitter.emit(new SetHandEvent(hand));

            emitter.emit(new SetPlayAreaEvent(playAreas.get(playerName)));

            emitter.emit(new SetPlayStatusEvent(playersInGame, playerColors, scores, connections));
        } else {
            emitter.emit(new GameResumedEvent());
        }
    }

    private void handleMessage(KickedFromGameS2C m) {
        changeState(ClientState.AUTHENTICATED);

        //clear all data related to game just deleted
        clearData();

        //TODO: remove or change
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Game was cancelled as there were not enough players connected!");
        alert.show();
    }

    private void handleMessage(NewMessageS2C m) {
        Message newMessage = new Message(m.messageType(), m.sender(), playerName, m.content(), m.timestamp());

        messages.add(newMessage);

        emitter.emit(new NewMessageEvent(
                newMessage
        ));
    }

    private void handleMessage(BroadcastMessageSentS2C m) {
        Message newMessage = new Message(MessageType.BROADCAST, m.sender(), playerName, m.content(), m.timestamp());

        messages.add(newMessage);

        emitter.emit(new NewMessageEvent(
                newMessage
        ));
    }

    private void handleMessage(DirectMessageSentS2C m) {
        Message newMessage = new Message(MessageType.DIRECT, m.sender(), m.recipient(), m.content(), m.timestamp());

        messages.add(newMessage);

        emitter.emit(new NewMessageEvent(
                newMessage
        ));
    }

    private void changeState(ClientState newState) {
        this.state = newState;
        emitter.emit(new StateChangedEvent(state, gameStatus));
    }

    private void changeGameStatus(GameStatus newStatus) {
        this.state = ClientState.IN_GAME;
        this.gameStatus = newStatus;
        emitter.emit(new StateChangedEvent(state, gameStatus));
    }

    public void exitFinishedGame() {
        changeState(ClientState.AUTHENTICATED);
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getStartingCardId() {
        return startingCardId;
    }

    public List<String> getPlayersInGame() {
        return playersInGame;
    }

    public List<Integer> getSecretObjectivesId() {
        return secretObjectivesId;
    }

    public SortedSet<Placement> getPlacements(String player) {
        //player part of the game as a pre-condition
        return playAreas.get(player);
    }

    public void removeLastPlacement(String player) {
        playAreas.get(player).removeLast();
    }

    public int getScore(String player) {
        //player part of the game as a pre-condition
        return scores.get(player);
    }

    public Map<Integer, UpdateGameListS2C.GameStat> getGames() {
        return games;
    }

    public PlayerColor getPlayerColor(String player) {
        return playerColors.get(player);
    }

    public int getMaxPlayers() {
        return games.get(gameId).maxPlayers();
    }

    public int getGameId() {
        return gameId;
    }

    public void setSecretObjectiveChoiceId(int secretObjectiveChoiceId) {
        this.secretObjectiveChoiceId = secretObjectiveChoiceId;
    }

    public boolean isConnected(String player) {
        return connections.get(player);
    }

    public List<Message> getMessages() {
        return messages;
    }

    private void clearData() {
        faceUpCards.clear();
        hand.clear();
        decksColor.clear();
        playAreas.clear();
        playersInGame.clear();
        playerColors.clear();
        scores.clear();
        secretObjectivesId.clear();
        connections.clear();
        gameStatus = null;
        turnPhase = null;
        currentPlayer = null;
        commonObjectivesId.clear();
        messages.clear();
    }

    private void sendMessage(C2SNetworkMessage message) {
        try {
            connection.send(message);
        } catch (SendNetworkException e) {
            throw new RuntimeException(e); //TODO: manage
        }
    }

    public void authenticate(String playerName) {
        sendMessage(new AuthenticateC2S(
                playerName
        ));
    }

    public void createGameAndJoin(int maxPlayers) {
        sendMessage(new CreateGameAndJoinC2S(
                maxPlayers
        ));
    }

    public void drawCardFromDeck(DeckLocation deckLocation) {
        sendMessage(new DrawCardFromDeckC2S(
                deckLocation
        ));
    }

    public void drawCardFromFaceUpCards(int cardId) {
        sendMessage(new DrawCardFromFaceUpCardsC2S(
                cardId
        ));
    }

    public void joinGame(int gameId) {
        sendMessage(new JoinGameC2S(
                gameId
        ));
    }

    public void placeCard(int cardId, Side side, int i, int j) {
        sendMessage(new PlaceCardC2S(
                cardId,
                side,
                i,
                j
        ));
    }

    public void resumeGame() {
        sendMessage(new ResumeGameC2S());
    }

    public void selectColor(PlayerColor color) {
        sendMessage(new SelectColorC2S(
                color
        ));
    }

    public void selectSecretObjective(int objective) {
        sendMessage(new SelectSecretObjectiveC2S(
                objective
        ));
    }

    public void selectStartingCardSide(Side side) {
        sendMessage(new SelectStartingCardSideC2S(
                side
        ));
    }

    public void sendBroadcastMessage(String content) {
        sendMessage(new SendBroadcastMessageC2S(
                content
        ));
    }

    public void sendDirectMessage(String recipientPlayerName, String content) {
        sendMessage(new SendDirectMessageC2S(
                recipientPlayerName,
                content
        ));
    }

    public void startGame() {
        sendMessage(new StartGameC2S());
    }

    @Override
    public Registration onAny(EventListener<ViewEvent> listener) {
        return emitter.onAny(listener);
    }

    @Override
    public <T extends ViewEvent> Registration on(Class<T> eventClass, EventListener<T> listener) {
        return emitter.on(eventClass, listener);
    }

    @Override
    public boolean unregister(Registration registration) {
        return emitter.unregister(registration);
    }


    public enum ConnectionType {
        TCP,
        RMI
    }

    public record Message(MessageType type, String sender, String recipient, String content, String timestamp) {
    }
}
