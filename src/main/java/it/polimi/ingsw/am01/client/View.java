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
import it.polimi.ingsw.am01.network.CloseNetworkException;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.OpenConnectionNetworkException;
import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.c2s.*;
import it.polimi.ingsw.am01.network.message.s2c.*;
import it.polimi.ingsw.am01.network.rmi.client.ClientRMIConnection;
import it.polimi.ingsw.am01.network.tcp.client.ClientTCPConnection;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public abstract class View implements EventEmitter<ViewEvent> {

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
    private final List<String> playersHaveChosenSecretObjective;
    private final Map<String, Integer> finalScores;
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

    public View() {
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
        this.finalScores = new HashMap<>();
        this.playersHaveChosenSecretObjective = new ArrayList<>();
        this.state = ClientState.NOT_CONNECTED;
    }

    protected abstract void changeStage(ClientState state, GameStatus gameStatus);

    protected abstract void kickPlayer();

    protected abstract void showConnectionErrorMessage(String errorMessage);

    public void connect(ConnectionType connectionType, String hostname, int port) {
        try {
            this.connection = switch (connectionType) {
                case ConnectionType.TCP -> ClientTCPConnection.connect(InetAddress.getByName(hostname), port);
                case ConnectionType.RMI -> ClientRMIConnection.connect(executorService, hostname, port);
            };

            new ConnectionWrapper(connection, this);

            state = ClientState.NOT_AUTHENTICATED;
            changeStage(state, gameStatus);
        } catch (OpenConnectionNetworkException | IOException | IllegalArgumentException e) {
            state = ClientState.NOT_CONNECTED;
            changeStage(state, gameStatus);
            showConnectionErrorMessage(e.getMessage());
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

    public abstract void runLater(Runnable runnable);

    public void handleMessage(SetPlayerNameS2C message) {
        this.playerName = message.playerName();
        changeState(ClientState.AUTHENTICATED);
    }

    public void handleMessage(NameAlreadyTakenS2C message) {
        emitter.emit(new NameAlreadyTakenEvent(message.refusedName()));
    }

    public void handleMessage(UpdateGameListS2C message) {
        this.games = message.gamesStatMap();
        emitter.emit(new GameListChangedEvent(games));
    }

    public void handleMessage(SetPlayablePositionsS2C message) {
        List<Position> playablePositions = message
                .playablePositions()
                .stream()
                .map(playablePosition -> new Position(playablePosition.i(), playablePosition.j()))
                .toList();
        emitter.emit(new UpdatePlayablePositionsEvent(playablePositions));
    }

    public void handleMessage(UpdatePlayAreaS2C message) {
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

    public void handleMessage(InvalidPlacementS2C message) {
        emitter.emit(new InvalidPlacementEvent());
    }

    public void handleMessage(UpdatePlayAreaAfterUndoS2C message) {
        scores.replace(message.profile(), message.score());
        emitter.emit(new RemoveLastPlacementEvent(message.profile()));
    }

    public void handleMessage(SetBoardAndHandS2C message) {
        //Set Objectives:
        commonObjectivesId = new ArrayList<>(message.commonObjectives());

        //Set board
        faceUpCards.clear();
        faceUpCards.addAll(message.faceUpCards());
        hand.clear();
        hand.addAll(message.hand());
        decksColor.put(DeckLocation.RESOURCE_CARD_DECK, message.resourceDeckColor());
        decksColor.put(DeckLocation.GOLDEN_CARD_DECK, message.goldenDeckColor());

        setupBoard();

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

    public void handleMessage(UpdatePlayerHandS2C message) {
        hand.clear();
        hand.addAll(message.currentHand());
        emitter.emit(new SetHandEvent(hand));
    }

    public void handleMessage(UpdateDeckStatusS2C message) {
        decksColor.replace(DeckLocation.RESOURCE_CARD_DECK, message.resourceDeckColor());
        decksColor.replace(DeckLocation.GOLDEN_CARD_DECK, message.goldenDeckColor());
        emitter.emit(new SetDeckEvent(
                Optional.of(decksColor.get(DeckLocation.GOLDEN_CARD_DECK)),
                Optional.of(decksColor.get(DeckLocation.RESOURCE_CARD_DECK))
        ));
    }

    public void handleMessage(UpdateGameStatusAndTurnS2C message) {
        this.currentPlayer = message.currentPlayerName();
        this.gameStatus = message.gameStatus();
        this.turnPhase = message.turnPhase();
        emitter.emit(new UpdateGameTurnEvent(playerName, currentPlayer, turnPhase.toString(), gameStatus.toString()));
    }

    public void handleMessage(UpdateFaceUpCardsS2C message) {
        faceUpCards.clear();
        faceUpCards.addAll(message.faceUpCards());
        emitter.emit(new SetFaceUpCardsEvent(faceUpCards.stream().toList()));
    }

    public void handleMessage(GameJoinedS2C message) {
        gameId = message.gameId();
        changeGameStatus(GameStatus.AWAITING_PLAYERS);
    }

    public void handleMessage(UpdatePlayerListS2C m) {
        playersInGame.clear();
        playersInGame.addAll(m.playerList());
        connections.clear();
        playersInGame.forEach(p -> connections.put(p, true));
        emitter.emit(new PlayerListChangedEvent(m.playerList()));
    }

    public void handleMessage(SetStartingCardS2C m) {
        startingCardId = m.startingCardId();
        changeGameStatus(GameStatus.SETUP_STARTING_CARD_SIDE);
    }

    public void handleMessage(UpdateGameStatusS2C m) {
        if (m.gameStatus().equals(GameStatus.SETUP_COLOR)) {
            changeGameStatus(GameStatus.SETUP_COLOR);
        }
    }

    public void handleMessage(UpdatePlayerColorS2C m) {
        playerColors.put(m.player(), m.color());
        emitter.emit(new UpdatePlayerColorEvent(
                m.player(),
                m.color()
        ));
    }

    public void handleMessage(UpdateGameStatusAndSetupObjectiveS2C m) {
        secretObjectivesId.addAll(
                List.of(
                        m.objectiveId1(), m.objectiveId2()
                )
        );
        changeGameStatus(GameStatus.SETUP_OBJECTIVE);
    }

    public void handleMessage(UpdateObjectiveSelectedS2C m) {
        playersHaveChosenSecretObjective.clear();
        playersHaveChosenSecretObjective.addAll(m.playersHaveChosen());
        emitter.emit(new UpdateSecretObjectiveChoiceEvent(
                m.playersHaveChosen().stream().toList()
        ));
    }

    public void handleMessage(PlayerDisconnectedS2C m) {
        if (connections.get(m.playerName()) != null) {
            connections.replace(m.playerName(), false);
        }
        emitter.emit(new SetPlayStatusEvent(playersInGame, playerColors, scores, connections));
    }

    public void handleMessage(PlayerReconnectedS2C m) {
        connections.replace(m.playerName(), true);
        emitter.emit(new SetPlayStatusEvent(playersInGame, playerColors, scores, connections));
    }

    public void handleMessage(GameFinishedS2C m) {
        changeGameStatus(GameStatus.FINISHED);
        finalScores.clear();
        finalScores.putAll(m.finalScores());
        emitter.emit(new SetFinalScoresEvent(m.finalScores(), playerColors));
        clearData();
    }

    public void handleMessage(SetupAfterReconnectionS2C m) {
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
            setupBoard();
            emitter.emit(new SetPlayAreaEvent(playAreas.get(playerName)));
        }

        emitter.emit(new SetPlayStatusEvent(playersInGame, playerColors, scores, connections));
    }

    private void setupBoard() {
        changeGameStatus(GameStatus.PLAY);
        emitter.emit(new SetObjectives(commonObjectivesId, secretObjectiveChoiceId));
        emitter.emit(new SetFaceUpCardsEvent(faceUpCards.stream().toList()));
        emitter.emit(new SetDeckEvent(
                Optional.of(decksColor.get(DeckLocation.GOLDEN_CARD_DECK)),
                Optional.of(decksColor.get(DeckLocation.RESOURCE_CARD_DECK))
        ));
        emitter.emit(new SetHandEvent(hand));
    }

    public void handleMessage(SetGamePauseS2C m) {
        gameStatus = m.getGameStatus();
        emitter.emit(new GamePausedEvent());
    }

    public void handleMessage(GameResumedS2C m) {
        if (gameStatus == GameStatus.RESTORING) {
            setupBoard();
            emitter.emit(new SetPlayAreaEvent(playAreas.get(playerName)));
            emitter.emit(new SetPlayStatusEvent(playersInGame, playerColors, scores, connections));
        } else {
            emitter.emit(new GameResumedEvent());
        }
    }

    public void handleMessage(KickedFromGameS2C m) {
        changeState(ClientState.AUTHENTICATED);
        //clear all data related to game just deleted
        clearData();
        kickPlayer();
    }

    public void handleMessage(NewMessageS2C m) {
        Message newMessage = new Message(m.messageType(), m.sender(), playerName, m.content(), m.timestamp());
        messages.add(newMessage);
        emitter.emit(new NewMessageEvent(newMessage));
    }

    public void handleMessage(BroadcastMessageSentS2C m) {
        Message newMessage = new Message(MessageType.BROADCAST, m.sender(), playerName, m.content(), m.timestamp());
        messages.add(newMessage);
        emitter.emit(new NewMessageEvent(newMessage));
    }

    public void handleMessage(DirectMessageSentS2C m) {
        Message newMessage = new Message(MessageType.DIRECT, m.sender(), m.recipient(), m.content(), m.timestamp());
        messages.add(newMessage);
        emitter.emit(new NewMessageEvent(newMessage));
    }

    private void changeState(ClientState newState) {
        this.state = newState;
        changeStage(state, gameStatus);
    }

    private void changeGameStatus(GameStatus newStatus) {
        this.state = ClientState.IN_GAME;
        this.gameStatus = newStatus;
        changeStage(state, gameStatus);
    }

    public void exitFinishedGame() {
        changeState(ClientState.AUTHENTICATED);
        finalScores.clear();
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

    public Map<String, Placement> getStartingCardPlacements() {
        return playAreas.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> entry.getValue().getFirst()
        ));
    }

    public SortedMap<String, Integer> getFinalPlacements() {
        SortedMap<String, Integer> finalPlacements = new TreeMap<>();

        List<Map.Entry<String, Integer>> orderedScores = finalScores.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).toList();
        for (int i = 0; i < orderedScores.size(); i++) {
            String player = orderedScores.get(i).getKey();
            int points = orderedScores.get(i).getValue();
            int placement = i > 0 && finalPlacements.values().stream().toList().get(i - 1) == points
                    ? finalPlacements.values().stream().toList().get(i - 1)
                    : i + 1;
            finalPlacements.put(player, placement);
        }
        return finalPlacements;
    }

    public void removeLastPlacement(String player) {
        playAreas.get(player).removeLast();
    }

    public int getScore(String player) {
        //player part of the game as a pre-condition
        return scores.get(player);
    }

    public List<String> getPlayersHaveChosenSecretObjective() {
        return playersHaveChosenSecretObjective;
    }

    public Map<String, Integer> getFinalScores() {
        return finalScores;
    }

    public Map<Integer, UpdateGameListS2C.GameStat> getGames() {
        return games;
    }

    public PlayerColor getPlayerColor(String player) {
        return playerColors.get(player);
    }

    public Map<String, PlayerColor> getPlayerColors() {
        return playerColors;
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

    public ClientState getState() {
        return state;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
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
        playersHaveChosenSecretObjective.clear();
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
