package it.polimi.ingsw.am01.client.gui;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.scene.*;
import it.polimi.ingsw.am01.model.game.GameStatus;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class GUIView extends View {
    public final ConnectionController CONNECTION_CONTROLLER;
    public final AuthController AUTH_CONTROLLER;
    public final GameListController GAME_LIST_CONTROLLER;
    public final PlayAreaController PLAY_CONTROLLER;
    public final LobbyController LOBBY_CONTROLLER;
    public final SelectStartingCardSideController STARTING_CARD_SIDE_CHOICE_CONTROLLER;
    public final SelectPlayerColorController PLAYER_COLOR_CHOICE_CONTROLLER;
    public final SelectObjectiveController OBJECTIVE_CHOICE_CONTROLLER;
    public final RestoringLobbyController RESTORING_LOBBY_CONTROLLER;
    public final EndingController ENDING_CONTROLLER;
    private final Stage stage;
    private SceneController currentSceneController;

    public GUIView(Stage stage) {
        this.CONNECTION_CONTROLLER = new ConnectionController(this);
        this.AUTH_CONTROLLER = new AuthController(this);
        this.GAME_LIST_CONTROLLER = new GameListController(this);
        this.PLAY_CONTROLLER = new PlayAreaController(this);
        this.LOBBY_CONTROLLER = new LobbyController(this);
        this.STARTING_CARD_SIDE_CHOICE_CONTROLLER = new SelectStartingCardSideController(this);
        this.PLAYER_COLOR_CHOICE_CONTROLLER = new SelectPlayerColorController(this);
        this.OBJECTIVE_CHOICE_CONTROLLER = new SelectObjectiveController(this);
        this.ENDING_CONTROLLER = new EndingController(this);
        this.RESTORING_LOBBY_CONTROLLER = new RestoringLobbyController(this);
        this.stage = stage;

        stage.setOnCloseRequest((e) -> {
            Platform.exit();
            this.closeConnection();
        });

        CONNECTION_CONTROLLER.loadScene(stage, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        currentSceneController = CONNECTION_CONTROLLER;
    }

    @Override
    public void runLater(Runnable runnable) {
        Platform.runLater(runnable);
    }

    @Override
    protected void showConnectionErrorMessage(String message) {
        CONNECTION_CONTROLLER.setErrorMessage(message);
    }

    @Override
    protected void kickPlayer() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Game was cancelled as there were not enough players connected!");
        alert.show();
    }

    @Override
    protected void changeStage(ClientState state, GameStatus gameStatus) {
        currentSceneController.getViewRegistrations().forEach(this::unregister);
        currentSceneController.getViewRegistrations().clear();

        SceneController newSceneController = switch (state) {
            case NOT_CONNECTED -> CONNECTION_CONTROLLER;
            case NOT_AUTHENTICATED -> AUTH_CONTROLLER;
            case AUTHENTICATED -> GAME_LIST_CONTROLLER;
            case IN_GAME -> switch (gameStatus) {
                case AWAITING_PLAYERS -> LOBBY_CONTROLLER;
                case SETUP_STARTING_CARD_SIDE -> STARTING_CARD_SIDE_CHOICE_CONTROLLER;
                case SETUP_COLOR -> PLAYER_COLOR_CHOICE_CONTROLLER;
                case SETUP_OBJECTIVE -> OBJECTIVE_CHOICE_CONTROLLER;
                case PLAY, SECOND_LAST_TURN, LAST_TURN, SUSPENDED -> PLAY_CONTROLLER;
                case FINISHED -> ENDING_CONTROLLER;
                case RESTORING -> RESTORING_LOBBY_CONTROLLER;
            };
        };

        if (newSceneController.equals(CONNECTION_CONTROLLER) && !currentSceneController.equals(CONNECTION_CONTROLLER)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Lost connection!");
            alert.show();
        }

        newSceneController.loadScene(this.stage, stage.getScene().getWidth(), stage.getScene().getHeight());
        currentSceneController = newSceneController;

        stage.setMaximized(true);

        if (newSceneController.equals(PLAY_CONTROLLER)) {
            stage.setFullScreen(true);
        }
    }
}
