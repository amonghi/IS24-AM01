package it.polimi.ingsw.am01.client;

import it.polimi.ingsw.am01.client.gui.event.*;
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
import it.polimi.ingsw.am01.network.message.NetworkMessage;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The common view for both graphical user interface and textual user interface.
 * It keeps a reduced and local copy of model data.
 * <p>
 * It is the client-side point to manage communication with the server:
 * <ul>
 *     <li> It handles the network messages received from the server and captured by
 *          the {@link ConnectionWrapper} class
 *     </li>
 *     <li> It sends network messages to the server, based on the interaction of
 *          the user with the GUI or the TUI
 *     </li>
 * </ul>
 *
 * @see ConnectionWrapper
 * @see EventEmitter
 * @see it.polimi.ingsw.am01.client.gui.GUIView
 * @see it.polimi.ingsw.am01.client.tui.TuiView
 */
public abstract class View implements EventEmitter<ViewEvent> {
    private static final Logger LOGGER = Logger.getLogger(View.class.getName());

    private final ExecutorService executorService;
    private final EventEmitterImpl<ViewEvent> emitter;

    private final Set<Integer> faceUpCards;
    private final Set<Integer> hand;
    private final Map<DeckLocation, CardColor> decksColor;
    private final Map<String, SortedSet<Placement>> playAreas;
    private final List<String> playersInGame;
    private final List<Position> playablePositions;
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

    /**
     * It constructs a new View initializing all the data
     */
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
        this.playablePositions = new ArrayList<>();
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

    /**
     * It handles the changing of the scene for the main stage
     *
     * @param state      The current {@link ClientState}
     * @param gameStatus The current {@link GameStatus}
     */
    protected abstract void changeStage(ClientState state, GameStatus gameStatus);

    /**
     * Gets called to notify that the player has been kicked from the game.
     * <p>
     * The only reason why this would happen is
     * that there aren't enough players connected during the initial selection phase,
     * so the game is canceled.
     */
    protected abstract void kickPlayer();

    /**
     * It shows an error message, in case of connection problems
     *
     * @param errorMessage The error message to be shown.
     */
    protected abstract void showConnectionErrorMessage(String errorMessage);

    /**
     * It calls the {@link ClientTCPConnection#connect(InetAddress, int)} or
     * {@link ClientRMIConnection#connect(ExecutorService, String, int)} method
     * according to the specified {@link ConnectionType}.
     * <p>
     * It also instantiates a new {@link ConnectionWrapper} to receive the network
     * message from the server
     *
     * @param connectionType The {@link ConnectionType} selected by the player.
     *                       It can be either {@link ConnectionType#TCP} or {@link ConnectionType#RMI}
     * @param hostname       The ip address of the server
     * @param port           The port of the server according to the selected {@link ConnectionType}
     */
    public void connect(ConnectionType connectionType, String hostname, int port) {
        try {
            this.connection = switch (connectionType) {
                case ConnectionType.TCP -> ClientTCPConnection.connect(InetAddress.getByName(hostname), port);
                case ConnectionType.RMI -> ClientRMIConnection.connect(executorService, hostname, port);
            };

            new ConnectionWrapper(connection, this);

            changeState(ClientState.NOT_AUTHENTICATED);
        } catch (OpenConnectionNetworkException | IOException | IllegalArgumentException e) {
            changeState(ClientState.NOT_CONNECTED);
            showConnectionErrorMessage(e.getMessage());
        }
    }

    /**
     * It calls the {@link ExecutorService#shutdown()} and also closes the
     * connection, calling the {@link Connection#close()} method
     */
    public void closeConnection() {
        if (connection != null) {
            executorService.shutdown();
            try {
                connection.close();
            } catch (CloseNetworkException e) {
                LOGGER.log(Level.WARNING, "Exception while closing connection", e);
            }
        }
    }

    /**
     * It permits to run the specified {@link Runnable} on a different thread
     *
     * @param runnable The {@link Runnable} that has to be run on a different thread.
     */
    public abstract void runLater(Runnable runnable);

    /**
     * It handles the specified message, updating the local model
     * and calling the {@link #changeState(ClientState)} method
     *
     * @param message The message received from the server
     * @see SetPlayerNameS2C
     */
    public void handleMessage(SetPlayerNameS2C message) {
        this.playerName = message.playerName();
        changeState(ClientState.AUTHENTICATED);
    }

    /**
     * It handles the specified message emitting an event to notify the interested classes
     *
     * @param message The message received from the server
     * @see NameAlreadyTakenS2C
     */
    public void handleMessage(NameAlreadyTakenS2C message) {
        emitter.emit(new NameAlreadyTakenEvent(message.refusedName()));
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param message The message received from the server
     * @see UpdateGameListS2C
     */
    public void handleMessage(UpdateGameListS2C message) {
        this.games = message.gamesStatMap();
        emitter.emit(new GameListChangedEvent(games));
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param message The message received from the server
     * @see SetPlayablePositionsS2C
     */
    public void handleMessage(SetPlayablePositionsS2C message) {
        playablePositions.clear();
        playablePositions.addAll(message
                .playablePositions()
                .stream()
                .map(playablePosition -> new Position(playablePosition.i(), playablePosition.j()))
                .toList());
        emitter.emit(new UpdatePlayablePositionsEvent(playablePositions));
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param message The message received from the server
     * @see UpdatePlayAreaS2C
     */
    public void handleMessage(UpdatePlayAreaS2C message) {
        playablePositions.clear();
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

    /**
     * It handles the specified message emitting an event to notify the interested classes
     *
     * @param message The message received from the server
     * @see InvalidPlacementS2C
     */
    public void handleMessage(InvalidPlacementS2C message) {
        emitter.emit(new InvalidPlacementEvent());
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param message The message received from the server
     * @see UpdatePlayAreaAfterUndoS2C
     */
    public void handleMessage(UpdatePlayAreaAfterUndoS2C message) {
        scores.replace(message.profile(), message.score());
        emitter.emit(new RemoveLastPlacementEvent(message.profile()));
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting events to notify the interested classes
     *
     * @param message The message received from the server
     * @see SetBoardAndHandS2C
     */
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

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param message The message received from the server
     * @see UpdatePlayerHandS2C
     */
    public void handleMessage(UpdatePlayerHandS2C message) {
        hand.clear();
        hand.addAll(message.currentHand());
        emitter.emit(new SetHandEvent(hand));
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param message The message received from the server
     * @see UpdateDeckStatusS2C
     */
    public void handleMessage(UpdateDeckStatusS2C message) {
        decksColor.replace(DeckLocation.RESOURCE_CARD_DECK, message.resourceDeckColor());
        decksColor.replace(DeckLocation.GOLDEN_CARD_DECK, message.goldenDeckColor());
        emitter.emit(new SetDeckEvent(
                Optional.ofNullable(decksColor.get(DeckLocation.GOLDEN_CARD_DECK)),
                Optional.ofNullable(decksColor.get(DeckLocation.RESOURCE_CARD_DECK))
        ));
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param message The message received from the server
     * @see UpdateGameStatusAndTurnS2C
     */
    public void handleMessage(UpdateGameStatusAndTurnS2C message) {
        this.currentPlayer = message.currentPlayerName();
        this.gameStatus = message.gameStatus();
        this.turnPhase = message.turnPhase();
        emitter.emit(new UpdateGameTurnEvent(playerName, currentPlayer, turnPhase.toString(), gameStatus.toString()));
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param message The message received from the server
     * @see UpdateFaceUpCardsS2C
     */
    public void handleMessage(UpdateFaceUpCardsS2C message) {
        faceUpCards.clear();
        faceUpCards.addAll(message.faceUpCards());
        emitter.emit(new SetFaceUpCardsEvent(faceUpCards.stream().toList()));
    }

    /**
     * It handles the specified message, updating the local model
     * and calling the {@link #changeGameStatus(GameStatus)} method
     *
     * @param message The message received from the server
     * @see GameJoinedS2C
     */
    public void handleMessage(GameJoinedS2C message) {
        gameId = message.gameId();
        changeGameStatus(GameStatus.AWAITING_PLAYERS);
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param m The message received from the server
     * @see UpdatePlayerListS2C
     */
    public void handleMessage(UpdatePlayerListS2C m) {
        playersInGame.clear();
        playersInGame.addAll(m.playerList());
        connections.clear();
        playersInGame.forEach(p -> connections.put(p, true));
        emitter.emit(new PlayerListChangedEvent(m.playerList()));
    }

    /**
     * It handles the specified message, updating the local model
     * and calling the {@link #changeGameStatus(GameStatus)} method
     *
     * @param m The message received from the server
     * @see SetStartingCardS2C
     */
    public void handleMessage(SetStartingCardS2C m) {
        startingCardId = m.startingCardId();
        changeGameStatus(GameStatus.SETUP_STARTING_CARD_SIDE);
    }

    /**
     * It handles the specified message calling the {@link #changeGameStatus(GameStatus)} method
     *
     * @param m The message received from the server
     * @see UpdateGameStatusS2C
     */
    public void handleMessage(UpdateGameStatusS2C m) {
        if (m.gameStatus().equals(GameStatus.SETUP_COLOR)) {
            changeGameStatus(GameStatus.SETUP_COLOR);
        }
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param m The message received from the server
     * @see UpdatePlayerColorS2C
     */
    public void handleMessage(UpdatePlayerColorS2C m) {
        playerColors.put(m.player(), m.color());
        emitter.emit(new UpdatePlayerColorEvent(
                m.player(),
                m.color()
        ));
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param m The message received from the server
     * @see UpdateGameStatusAndSetupObjectiveS2C
     */
    public void handleMessage(UpdateGameStatusAndSetupObjectiveS2C m) {
        secretObjectivesId.addAll(
                List.of(
                        m.objectiveId1(), m.objectiveId2()
                )
        );
        changeGameStatus(GameStatus.SETUP_OBJECTIVE);
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param m The message received from the server
     * @see UpdateObjectiveSelectedS2C
     */
    public void handleMessage(UpdateObjectiveSelectedS2C m) {
        playersHaveChosenSecretObjective.clear();
        playersHaveChosenSecretObjective.addAll(m.playersHaveChosen());
        emitter.emit(new UpdateSecretObjectiveChoiceEvent(
                m.playersHaveChosen().stream().toList()
        ));
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param m The message received from the server
     * @see PlayerDisconnectedS2C
     */
    public void handleMessage(PlayerDisconnectedS2C m) {
        if (connections.get(m.playerName()) != null) {
            connections.replace(m.playerName(), false);
        }
        emitter.emit(new SetPlayStatusEvent(playersInGame, playerColors, scores, connections));
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param m The message received from the server
     * @see PlayerReconnectedS2C
     */
    public void handleMessage(PlayerReconnectedS2C m) {
        connections.replace(m.playerName(), true);
        emitter.emit(new SetPlayStatusEvent(playersInGame, playerColors, scores, connections));
    }

    /**
     * It handles the specified message, calling the {@link #changeGameStatus(GameStatus)},
     * updating the local model and emitting an event to notify the interested classes
     *
     * @param m The message received from the server
     * @see GameFinishedS2C
     */
    public void handleMessage(GameFinishedS2C m) {
        changeGameStatus(GameStatus.FINISHED);
        finalScores.clear();
        finalScores.putAll(m.finalScores());
        emitter.emit(new SetFinalScoresEvent(m.finalScores(), playerColors));
        clearData();
    }

    /**
     * It handles the specified message setting all the data of local model based on
     * the information received from the server.
     * It also emits events to notify the interested classes
     *
     * @param m The message received from the server
     * @see SetupAfterReconnectionS2C
     */
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

    /**
     * It emits all the necessary events to update the board of the player,
     * based on the local model
     */
    private void setupBoard() {
        changeGameStatus(GameStatus.PLAY);
        emitter.emit(new SetObjectives(commonObjectivesId, secretObjectiveChoiceId));
        emitter.emit(new SetFaceUpCardsEvent(faceUpCards.stream().toList()));
        emitter.emit(new SetDeckEvent(
                Optional.ofNullable(decksColor.get(DeckLocation.GOLDEN_CARD_DECK)),
                Optional.ofNullable(decksColor.get(DeckLocation.RESOURCE_CARD_DECK))
        ));
        emitter.emit(new SetHandEvent(hand));
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting an event to notify the interested classes
     *
     * @param m The message received from the server
     * @see SetGamePauseS2C
     */
    public void handleMessage(SetGamePauseS2C m) {
        gameStatus = m.getGameStatus();
        emitter.emit(new GamePausedEvent());
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting events to notify the interested classes
     *
     * @param m The message received from the server
     * @see GameResumedS2C
     */
    public void handleMessage(GameResumedS2C m) {
        if (gameStatus == GameStatus.RESTORING) {
            setupBoard();
            emitter.emit(new SetPlayAreaEvent(playAreas.get(playerName)));
            emitter.emit(new SetPlayStatusEvent(playersInGame, playerColors, scores, connections));
        } else {
            emitter.emit(new GameResumedEvent());
        }
    }

    /**
     * It handles the specified message, calling he {@link #changeState(ClientState)} method
     * and clearing all the data related to game just deleted
     *
     * @param m The message received from the server
     * @see KickedFromGameS2C
     */
    public void handleMessage(KickedFromGameS2C m) {
        changeState(ClientState.AUTHENTICATED);
        clearData();
        kickPlayer();
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting events to notify the interested classes
     *
     * @param m The message received from the server
     * @see NewMessageS2C
     * @see Message
     */
    public void handleMessage(NewMessageS2C m) {
        Message newMessage = new Message(m.messageType(), m.sender(), playerName, m.content(), m.timestamp());
        messages.add(newMessage);
        emitter.emit(new NewMessageEvent(newMessage));
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting events to notify the interested classes
     *
     * @param m The message received from the server
     * @see BroadcastMessageSentS2C
     * @see Message
     */
    public void handleMessage(BroadcastMessageSentS2C m) {
        Message newMessage = new Message(MessageType.BROADCAST, m.sender(), playerName, m.content(), m.timestamp());
        messages.add(newMessage);
        emitter.emit(new NewMessageEvent(newMessage));
    }

    /**
     * It handles the specified message, updating the local model
     * and emitting events to notify the interested classes
     *
     * @param m The message received from the server
     * @see DirectMessageSentS2C
     * @see Message
     */
    public void handleMessage(DirectMessageSentS2C m) {
        Message newMessage = new Message(MessageType.DIRECT, m.sender(), m.recipient(), m.content(), m.timestamp());
        messages.add(newMessage);
        emitter.emit(new NewMessageEvent(newMessage));
    }

    /**
     * It handles the specified message, updating the local model
     * and calling the {@link #changeStage(ClientState, GameStatus)} method
     *
     * @param newState The new state of the client
     * @see ClientState
     */
    private void changeState(ClientState newState) {
        this.state = newState;
        changeStage(state, gameStatus);
    }

    /**
     * It set the {@link ClientState} to {@link ClientState#NOT_CONNECTED}
     *
     * @see #runLater(Runnable)
     * @see #clearData()
     */
    public void connectionLost() {
        runLater(() -> changeState(ClientState.NOT_CONNECTED));
        clearData();
    }

    /**
     * It updates the current {@link GameStatus}.
     * It also calls the {@link #changeStage(ClientState, GameStatus)}
     *
     * @param newStatus the new {@link GameStatus} to set
     */
    private void changeGameStatus(GameStatus newStatus) {
        this.state = ClientState.IN_GAME;
        this.gameStatus = newStatus;
        changeStage(state, gameStatus);
    }

    /**
     * It updates the current {@link ClientState}.
     */
    public void exitFinishedGame() {
        changeState(ClientState.AUTHENTICATED);
        finalScores.clear();
    }

    /**
     * @return The name of the owner of the view
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @return The id of the starting card
     */
    public int getStartingCardId() {
        return startingCardId;
    }

    /**
     * @return The list of the players who have joined the game
     */
    public List<String> getPlayersInGame() {
        return playersInGame;
    }

    /**
     * @return The ids of the choose-able secret objectives
     */
    public List<Integer> getSecretObjectivesId() {
        return secretObjectivesId;
    }

    /**
     * @param player The player whose play area you want to see. It has to be a player in the game
     * @return The set of {@link Placement}, sorted by their sequence number
     */
    public SortedSet<Placement> getPlacements(String player) {
        //player part of the game as a pre-condition
        return playAreas.get(player);
    }

    /**
     * @return The starting card placement for every player
     */
    public Map<String, Placement> getStartingCardPlacements() {
        return playAreas.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> entry.getValue().getFirst()
        ));
    }

    /**
     * @return the final ranking, when the game is over
     */
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

    /**
     * It removes the last placement from the play area of the specified player
     *
     * @param player The player whose placement you want to remove. It has to be a player in the game
     */
    public void removeLastPlacement(String player) {
        playAreas.get(player).removeLast();
    }

    /**
     * @param player The player name. It has to be a player in the game
     * @return the score of the specified player
     */
    public int getScore(String player) {
        //player part of the game as a pre-condition
        return scores.get(player);
    }

    /**
     * @return The list of the players who have already chosen their secret objectives
     */
    public List<String> getPlayersHaveChosenSecretObjective() {
        return playersHaveChosenSecretObjective;
    }

    /**
     * @return The final score of each player
     */
    public Map<String, Integer> getFinalScores() {
        return finalScores;
    }

    /**
     * @return The list of all the available games
     */
    public Map<Integer, UpdateGameListS2C.GameStat> getGames() {
        return games;
    }

    /**
     * @param player The player name. It has to be a player in the game
     * @return the {@link PlayerColor} of the specified player
     */
    public PlayerColor getPlayerColor(String player) {
        return playerColors.get(player);
    }

    /**
     * @return The {@link PlayerColor} chosen by each player
     */
    public Map<String, PlayerColor> getPlayerColors() {
        return playerColors;
    }

    /**
     * @return The maximum number of player allowed in the game
     */
    public int getMaxPlayers() {
        return games.get(gameId).maxPlayers();
    }

    /**
     * @return The id of the current game
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * @param player The player name. It has to be a player in the game
     * @return Whether a player is connected or not
     */
    public boolean isConnected(String player) {
        return connections.get(player);
    }

    /**
     * @return The list of all the chat messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * @return The current {@link ClientState}
     */
    public ClientState getState() {
        return state;
    }

    /**
     * @return The current {@link GameStatus}
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * @return The current {@link TurnPhase}
     */
    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    /**
     * @return The name of the player who is currently playing
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return The list of all the playable positions
     */
    public List<Position> getPlayablePositions() {
        return playablePositions;
    }

    /**
     * @return The color of the top card of each deck
     */
    public Map<DeckLocation, CardColor> getDecksColor() {
        return Collections.unmodifiableMap(decksColor);
    }

    /**
     * @param deckLocation The deck source, either {@link DeckLocation#GOLDEN_CARD_DECK} or {@link DeckLocation#RESOURCE_CARD_DECK}
     * @return Whether the specified deck is empty or not
     */
    public boolean isDeckEmpty(DeckLocation deckLocation) {
        return (decksColor.get(deckLocation) == null);
    }

    /**
     * @return The list of the cards in the hand of the player
     */
    public List<Integer> getHand() {
        return hand.stream().toList();
    }

    /**
     * @return The ids of the visible face up cards
     */
    public List<Integer> getFaceUpCards() {
        return faceUpCards.stream().toList();
    }

    /**
     * @return The ids of the common objectives
     */
    public List<Integer> getCommonObjectivesId() {
        return commonObjectivesId;
    }

    /**
     * @return The id of the secret objective chosen by the player
     */
    public int getSecretObjectiveChoiceId() {
        return secretObjectiveChoiceId;
    }

    /**
     * @param secretObjectiveChoiceId The id of the secret objective chosen by the player
     */
    public void setSecretObjectiveChoiceId(int secretObjectiveChoiceId) {
        this.secretObjectiveChoiceId = secretObjectiveChoiceId;
    }

    /**
     * It clears all the data related to this game
     */
    protected void clearData() {
        faceUpCards.clear();
        hand.clear();
        decksColor.clear();
        playAreas.clear();
        playersInGame.clear();
        playerColors.clear();
        playablePositions.clear();
        scores.clear();
        secretObjectivesId.clear();
        connections.clear();
        commonObjectivesId.clear();
        playersHaveChosenSecretObjective.clear();
        messages.clear();
    }

    /**
     * It calls the {@link Connection#send(NetworkMessage)}
     *
     * @param message The {@link C2SNetworkMessage} message to send to the server
     */
    private void sendMessage(C2SNetworkMessage message) {
        try {
            connection.send(message);
        } catch (SendNetworkException e) {
            connectionLost();
        }
    }

    /**
     * It sends a message to the server to authenticate the player
     *
     * @param playerName The name chosen by the player
     */
    public void authenticate(String playerName) {
        sendMessage(new AuthenticateC2S(
                playerName
        ));
    }

    /**
     * It sends a message to the server to create a new game
     *
     * @param maxPlayers The maximum number of players allowed in the game
     */
    public void createGameAndJoin(int maxPlayers) {
        sendMessage(new CreateGameAndJoinC2S(
                maxPlayers
        ));
    }

    /**
     * It sends a message to the draw a card from the specified deck location
     *
     * @param deckLocation The deck source from which draw a card
     */
    public void drawCardFromDeck(DeckLocation deckLocation) {
        sendMessage(new DrawCardFromDeckC2S(
                deckLocation
        ));
    }

    /**
     * It sends a message to the server to draw the specified face up card
     *
     * @param cardId The id of the card to draw
     */
    public void drawCardFromFaceUpCards(int cardId) {
        sendMessage(new DrawCardFromFaceUpCardsC2S(
                cardId
        ));
    }

    /**
     * It sends a message to the server to join a game
     *
     * @param gameId The id of the game to join
     */
    public void joinGame(int gameId) {
        sendMessage(new JoinGameC2S(
                gameId
        ));
    }

    /**
     * It sends a message to the server to place a card in the play area
     *
     * @param cardId The id of the card to place
     * @param side   The side of the card to place
     * @param i      The i-coordinate of the position where the card has to be placed
     * @param j      The j-coordinate of the position where the card has to be placed
     */
    public void placeCard(int cardId, Side side, int i, int j) {
        sendMessage(new PlaceCardC2S(
                cardId,
                side,
                i,
                j
        ));
    }

    /**
     * It sends a message to the server to resume the game
     */
    public void resumeGame() {
        sendMessage(new ResumeGameC2S());
    }

    /**
     * It sends a message to select the specified color
     *
     * @param color The {@link PlayerColor} the player wants to choose
     */
    public void selectColor(PlayerColor color) {
        sendMessage(new SelectColorC2S(
                color
        ));
    }

    /**
     * It sends a message to select the specified secret objective
     *
     * @param objective The id of the objective the player wants to choose
     */
    public void selectSecretObjective(int objective) {
        sendMessage(new SelectSecretObjectiveC2S(
                objective
        ));
    }

    /**
     * It sends a message to select the side of the starting card
     *
     * @param side The {@link Side} the player wants to choose
     */
    public void selectStartingCardSide(Side side) {
        sendMessage(new SelectStartingCardSideC2S(
                side
        ));
    }

    /**
     * It sends a message to send a new broadcast message
     *
     * @param content The content of the message to send to all the other players
     */
    public void sendBroadcastMessage(String content) {
        sendMessage(new SendBroadcastMessageC2S(
                content
        ));
    }

    /**
     * It sends a message to send a new direct message
     *
     * @param content             The content of the message to send to the specified player
     * @param recipientPlayerName The recipient of the message
     */
    public void sendDirectMessage(String recipientPlayerName, String content) {
        sendMessage(new SendDirectMessageC2S(
                recipientPlayerName,
                content
        ));
    }

    /**
     * It sends a message to start the current game
     */
    public void startGame() {
        sendMessage(new StartGameC2S());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Registration onAny(EventListener<ViewEvent> listener) {
        return emitter.onAny(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends ViewEvent> Registration on(Class<T> eventClass, EventListener<T> listener) {
        return emitter.on(eventClass, listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unregister(Registration registration) {
        return emitter.unregister(registration);
    }

    public enum ConnectionType {
        TCP,
        RMI
    }

    /**
     * A chat message
     *
     * @param type      The type of the message, either {@link MessageType#BROADCAST} or {@link MessageType#DIRECT}
     * @param sender    The sender of the message
     * @param recipient The recipient of the message
     * @param content   The content of the message
     * @param timestamp The time the player sent the message
     */
    public record Message(MessageType type, String sender, String recipient, String content, String timestamp) {
    }
}