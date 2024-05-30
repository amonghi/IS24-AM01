package it.polimi.ingsw.am01.client.gui;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.scene.*;
import it.polimi.ingsw.am01.client.gui.event.*;
import it.polimi.ingsw.am01.client.gui.model.GUIPlacement;
import it.polimi.ingsw.am01.client.gui.model.Position;
import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.eventemitter.EventEmitterImpl;
import it.polimi.ingsw.am01.eventemitter.EventListener;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.TurnPhase;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.ReceiveNetworkException;
import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.*;

public class GUIView implements EventEmitter<ViewEvent> {
    private static GUIView instance = null;
    private final EventEmitterImpl<ViewEvent> emitter;
    private final Connection<C2SNetworkMessage, S2CNetworkMessage> connection;
    private final Stage stage;
    private final AuthController AUTH_CONTROLLER;
    private final GameListController GAME_LIST_CONTROLLER;
    private final PlayAreaController PLAY_CONTROLLER;
    private final LobbyController LOBBY_CONTROLLER;
    private final SelectStartingCardSideController STARTING_CARD_SIDE_CHOICE_CONTROLLER;
    private final SelectPlayerColorController PLAYER_COLOR_CHOICE_CONTROLLER;
    private final SelectObjectiveController OBJECTIVE_CHOICE_CONTROLLER;
    private final RestoringLobbyController RESTORING_LOBBY_CONTROLLER;
    private final EndingController ENDING_CONTROLLER;
    private final Set<Integer> faceUpCards;
    private final Set<Integer> hand;
    private final Map<DeckLocation, CardColor> decksColor;
    private final Map<String, SortedSet<GUIPlacement>> playAreas;
    private final List<String> playersInGame;
    private final Map<String, PlayerColor> playerColors;
    private final Map<String, Integer> scores;
    private final List<Integer> secretObjectivesId;
    private final Map<String, Boolean> connections;
    private SceneController currentSceneController;
    private Map<Integer, UpdateGameListS2C.GameStat> games;
    private String playerName;
    private int gameId;
    private int startingCardId;
    private GameStatus gameStatus;
    private TurnPhase turnPhase;
    private String currentPlayer;
    private int secretObjectiveChoiceId;
    private List<Integer> commonObjectivesId;

    /*
        TODO:
        private List<Message> messages;
    */

    public GUIView(Connection<C2SNetworkMessage, S2CNetworkMessage> connection, Stage stage) {
        this.emitter = new EventEmitterImpl<>();
        this.stage = stage;
        this.connection = connection;
        instance = this;


        stage.setOnCloseRequest((e) -> {
            Platform.exit();
            GUIClient.closeClient();
            System.exit(0); //FIXME: fix
        });


        this.AUTH_CONTROLLER = new AuthController();
        this.GAME_LIST_CONTROLLER = new GameListController();
        this.PLAY_CONTROLLER = new PlayAreaController();
        this.LOBBY_CONTROLLER = new LobbyController();
        this.STARTING_CARD_SIDE_CHOICE_CONTROLLER = new SelectStartingCardSideController();
        this.PLAYER_COLOR_CHOICE_CONTROLLER = new SelectPlayerColorController();
        this.OBJECTIVE_CHOICE_CONTROLLER = new SelectObjectiveController();
        this.ENDING_CONTROLLER = new EndingController();
        this.RESTORING_LOBBY_CONTROLLER = new RestoringLobbyController();

        AUTH_CONTROLLER.loadScene(stage, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        currentSceneController = AUTH_CONTROLLER;

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
        //TODO: initialize others...

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
                            case PingS2C m -> {
                            }
                            default -> throw new IllegalStateException("Unexpected value: " + message); //TODO: manage
                        }
                    });
                } catch (ReceiveNetworkException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public static GUIView getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new RuntimeException(); //TODO: handle
        }
    }

    private void handleMessage(SetPlayerNameS2C message) {
        this.playerName = message.playerName();
        changeScene(GAME_LIST_CONTROLLER);
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

        playAreas.get(message.playerName()).add(new GUIPlacement(message.cardId(), message.side(), new Position(message.i(), message.j()), message.seq(), message.points()));
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

        stage.setFullScreen(true);
        changeScene(PLAY_CONTROLLER);


        emitter.emit(new SetObjectives(commonObjectivesId, secretObjectiveChoiceId));
        emitter.emit(new SetFaceUpCardsEvent(faceUpCards.stream().toList()));
        emitter.emit(new SetDeckEvent(
                Optional.of(decksColor.get(DeckLocation.GOLDEN_CARD_DECK)),
                Optional.of(decksColor.get(DeckLocation.RESOURCE_CARD_DECK))
        ));
        emitter.emit(new SetHandEvent(hand));

        GUIPlacement starterCard = playAreas.get(playerName).getFirst();

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
        changeScene(LOBBY_CONTROLLER);
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
        changeScene(STARTING_CARD_SIDE_CHOICE_CONTROLLER);
    }

    private void handleMessage(UpdateGameStatusS2C m) {
        if (m.gameStatus().equals(GameStatus.SETUP_COLOR)) {
            changeScene(PLAYER_COLOR_CHOICE_CONTROLLER);
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
        changeScene(OBJECTIVE_CHOICE_CONTROLLER);
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
        changeScene(ENDING_CONTROLLER);
        emitter.emit(new SetFinalScoresEvent(m.finalScores()));
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

        m.playAreas().forEach((player, pa) -> {
            playAreas.put(player, new TreeSet<>());
            pa.forEach(((position, cardPlacement) -> {
                playAreas.get(player).add(new GUIPlacement(
                        cardPlacement.cardId(),
                        cardPlacement.side(),
                        new Position(position.i(), position.j()),
                        cardPlacement.seq(),
                        cardPlacement.points()
                ));
            }));
        });
        // TODO: add chat messages

        playersInGame.forEach(player -> scores.put(player,
                playAreas.get(player).stream().mapToInt(GUIPlacement::points).sum()
        ));
        if(gameStatus == GameStatus.RESTORING){
            changeScene(RESTORING_LOBBY_CONTROLLER);
        } else {
            stage.setFullScreen(true);
            changeScene(PLAY_CONTROLLER);
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
        if(gameStatus == GameStatus.RESTORING){
            stage.setFullScreen(true);
            changeScene(PLAY_CONTROLLER);

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
        changeScene(GAME_LIST_CONTROLLER);

        //clear all data related to cancelled game
        faceUpCards.clear();
        hand.clear();
        playAreas.clear();
        playersInGame.clear();
        playerColors.clear();
        scores.clear();
        secretObjectivesId.clear();
        connections.clear();
        gameId = -1; //FIXME: can't set to null...
        startingCardId = -1; //FIXME: can't set to null...
        gameStatus = null;
        turnPhase = null;
        currentPlayer = null;
        secretObjectiveChoiceId = -1; //FIXME: can't set to null...
        commonObjectivesId.clear();

        //TODO: remove or change
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Game was cancelled as there were not enough players connected!");
        alert.show();
    }

    private void changeScene(SceneController newSceneController) {
        currentSceneController.getViewRegistrations().forEach(this::unregister);
        currentSceneController.getViewRegistrations().clear();

        newSceneController.loadScene(this.stage, stage.getScene().getWidth(), stage.getScene().getHeight());
        currentSceneController = newSceneController;
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

    public SortedSet<GUIPlacement> getPlacements(String player) {
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

    public boolean isConnected(String player){
        return connections.get(player);
    }

    public PlayAreaController getPlayAreaController() {
        return PLAY_CONTROLLER;
    }

    public void sendMessage(C2SNetworkMessage message) {
        try {
            connection.send(message);
        } catch (SendNetworkException e) {
            throw new RuntimeException(e); //TODO: manage
        }
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
}