package it.polimi.ingsw.am01.client.gui;

import it.polimi.ingsw.am01.client.gui.controller.scene.AuthController;
import it.polimi.ingsw.am01.client.gui.controller.scene.GameListController;
import it.polimi.ingsw.am01.client.gui.controller.scene.SceneController;
import it.polimi.ingsw.am01.client.gui.event.GameListChangedEvent;
import it.polimi.ingsw.am01.client.gui.event.NameAlreadyTakenEvent;
import it.polimi.ingsw.am01.client.gui.event.ViewEvent;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.eventemitter.EventEmitterImpl;
import it.polimi.ingsw.am01.eventemitter.EventListener;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.ReceiveNetworkException;
import it.polimi.ingsw.am01.network.SendNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.NameAlreadyTakenS2C;
import it.polimi.ingsw.am01.network.message.s2c.PingS2C;
import it.polimi.ingsw.am01.network.message.s2c.SetPlayerNameS2C;
import it.polimi.ingsw.am01.network.message.s2c.UpdateGameListS2C;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class GUIView implements EventEmitter<ViewEvent> {
    private final EventEmitterImpl<ViewEvent> emitter;

    private final Connection<C2SNetworkMessage, S2CNetworkMessage> connection;
    private final Stage stage;


    private final AuthController AUTH_CONTROLLER;
    private final GameListController GAME_LIST_CONTROLLER;

    private SceneController currentSceneController;

    private Map<Integer, UpdateGameListS2C.GameStat> games; //FIXME: maybe useless

    private String playerName;

    /*
    private int gameId;

    private List<String> playersInGame;
    private Map<String, PlayerColor> playerColors;
    private Map<String, PlayArea> playAreas;
    private Map<String, Integer> scores;

    private GameStatus gameStatus;
    private TurnPhase turnPhase;
    private String currentPlayer;

    private Map<String, Boolean> secretObjectiveSelection;

    private List<Objective> commonObjectives;
    private List<FaceUpCard> faceUpCards;
    private List<Card> hand;
    private Objective secretObjective;
    private Map<DeckLocation, Boolean> decksAreEmpty;

    private List<SetPlayablePositionsS2C.PlayablePosition> playablePositions; //FIXME: maybe useless
    */

    public GUIView(Connection<C2SNetworkMessage, S2CNetworkMessage> connection, Stage stage) {
        this.emitter = new EventEmitterImpl<>();
        this.stage = stage;
        this.connection = connection;


        stage.setOnCloseRequest((e) -> {
            Platform.exit();
            GUIClient.closeClient();
            System.exit(0); //FIXME: fix
        });


        this.AUTH_CONTROLLER = new AuthController(this);
        this.GAME_LIST_CONTROLLER = new GameListController(this);


        AUTH_CONTROLLER.loadScene(stage, "Codex Naturalis");
        currentSceneController = AUTH_CONTROLLER;

        this.games = new HashMap<>();
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

    private void handleMessage(SetPlayerNameS2C message) {
        this.playerName = message.playerName();
        changeScene(GAME_LIST_CONTROLLER, "Games List");
    }

    private void handleMessage(NameAlreadyTakenS2C message) {
        emitter.emit(new NameAlreadyTakenEvent(message.refusedName()));
    }

    private void handleMessage(UpdateGameListS2C message) {
        this.games = message.gamesStatMap();
        emitter.emit(new GameListChangedEvent(games));
    }

    private void changeScene(SceneController newSceneController, String newTitle) { //TODO: newTitle input or static value?
        currentSceneController.getViewRegistrations().forEach(this::unregister);
        currentSceneController.getViewRegistrations().clear();
        newSceneController.loadScene(this.stage, newTitle);
        currentSceneController = newSceneController;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void sendMessage(C2SNetworkMessage message) {
        try {
            connection.send(message);
        } catch (SendNetworkException e) {
            throw new RuntimeException(e); //TOOD: manage
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