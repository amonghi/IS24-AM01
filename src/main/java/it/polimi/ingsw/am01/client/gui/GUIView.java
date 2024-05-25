package it.polimi.ingsw.am01.client.gui;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.scene.*;
import it.polimi.ingsw.am01.client.gui.event.*;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.eventemitter.EventEmitterImpl;
import it.polimi.ingsw.am01.eventemitter.EventListener;
import it.polimi.ingsw.am01.model.game.GameAssets;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.ReceiveNetworkException;
import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.*;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private SceneController currentSceneController;
    private Map<Integer, UpdateGameListS2C.GameStat> games;
    private String playerName;
    private int gameId;

    private Map<String, PlayArea> playAreas;
    private int startingCardId;
    private List<String> playersInGame;

    private int secretObjectiveChoiceId;
    private List<Integer> secretObjectivesId;


    /*
    private List<Message> messages;
    private Map<String, PlayerColor> playerColors;
    private Map<String, Integer> scores;

    private GameStatus gameStatus;
    private TurnPhase turnPhase;
    private String currentPlayer;

    private Map<String, Boolean> secretObjectiveSelection;

    private List<Objective> commonObjectives;
    private List<FaceUpCard> faceUpCards;
    private List<Card> hand;
    private Map<DeckLocation, Boolean> decksAreEmpty;

    private List<SetPlayablePositionsS2C.PlayablePosition> playablePositions; //FIXME: maybe useless
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

        AUTH_CONTROLLER.loadScene(stage, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        currentSceneController = AUTH_CONTROLLER;

        this.games = new HashMap<>();
        this.playAreas = new HashMap<>();
        this.playersInGame = new ArrayList<>();
        this.secretObjectivesId = new ArrayList<>();
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
                            case PlayerDisconnectedS2C m -> {
                            }
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
        emitter.emit(new UpdatePlayAreaEvent(
                message.playerName(),
                message.i(),
                message.j(),
                message.cardId(),
                message.side(),
                message.seq(),
                message.points()
        ));

        if (!playAreas.containsKey(message.playerName())) {
            playAreas.put(message.playerName(), new PlayArea(GameAssets.getInstance().getCardById(message.cardId()).get(), message.side())); //TODO: manage get() exception
        }
    }

    private void handleMessage(InvalidPlacementS2C message) {
        emitter.emit(new InvalidPlacementEvent());
    }

    private void handleMessage(GameJoinedS2C message) {
        gameId = message.gameId();
        changeScene(LOBBY_CONTROLLER);
    }

    private void handleMessage(UpdatePlayerListS2C m) {
        emitter.emit(new PlayerListChangedEvent(m.playerList()));
        playersInGame.clear();
        playersInGame.addAll(m.playerList());
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

    public int getGameId() {
        return gameId;
    }

    public int getMaxPlayers() {
        return games.get(gameId).maxPlayers();
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