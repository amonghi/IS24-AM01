package it.polimi.ingsw.am01.client.gui;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.scene.*;
import it.polimi.ingsw.am01.client.gui.event.ConnectionLostEvent;
import it.polimi.ingsw.am01.client.gui.event.StateChangedEvent;
import javafx.application.Platform;
import javafx.stage.Stage;

public class GUIView {
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
    private SceneController currentSceneController;
    private final Stage stage;

    public GUIView(Stage stage) {
        this.CONNECTION_CONTROLLER = new ConnectionController();
        this.AUTH_CONTROLLER = new AuthController();
        this.GAME_LIST_CONTROLLER = new GameListController();
        this.PLAY_CONTROLLER = new PlayAreaController();
        this.LOBBY_CONTROLLER = new LobbyController();
        this.STARTING_CARD_SIDE_CHOICE_CONTROLLER = new SelectStartingCardSideController();
        this.PLAYER_COLOR_CHOICE_CONTROLLER = new SelectPlayerColorController();
        this.OBJECTIVE_CHOICE_CONTROLLER = new SelectObjectiveController();
        this.ENDING_CONTROLLER = new EndingController();
        this.RESTORING_LOBBY_CONTROLLER = new RestoringLobbyController();
        this.stage = stage;

        View.getInstance().on(StateChangedEvent.class, this::changeStage);
        View.getInstance().on(ConnectionLostEvent.class, this::showErrorMessage);

        stage.setOnCloseRequest((e) -> {
            Platform.exit();
            View.getInstance().closeConnection();
        });

        CONNECTION_CONTROLLER.loadScene(stage, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        currentSceneController = CONNECTION_CONTROLLER;

    }

    private void showErrorMessage(ConnectionLostEvent event) {
        Platform.runLater(() -> CONNECTION_CONTROLLER.setErrorMessage(event.message()));
    }

    private void changeStage(StateChangedEvent event) {
        currentSceneController.getViewRegistrations().forEach(View.getInstance()::unregister);
        currentSceneController.getViewRegistrations().clear();

        SceneController newSceneController = switch (event.state()) {
            case NOT_CONNECTED -> CONNECTION_CONTROLLER;
            case NOT_AUTHENTICATED -> AUTH_CONTROLLER;
            case AUTHENTICATED -> GAME_LIST_CONTROLLER;
            case IN_GAME -> switch (event.gameStatus()) {
                case AWAITING_PLAYERS -> LOBBY_CONTROLLER;
                case SETUP_STARTING_CARD_SIDE -> STARTING_CARD_SIDE_CHOICE_CONTROLLER;
                case SETUP_COLOR -> PLAYER_COLOR_CHOICE_CONTROLLER;
                case SETUP_OBJECTIVE -> OBJECTIVE_CHOICE_CONTROLLER;
                case PLAY, SECOND_LAST_TURN, LAST_TURN, SUSPENDED -> PLAY_CONTROLLER;
                case FINISHED -> ENDING_CONTROLLER;
                case RESTORING -> RESTORING_LOBBY_CONTROLLER;
            };
        };

        if (newSceneController.equals(PLAY_CONTROLLER)) {
            Platform.runLater(() -> stage.setFullScreen(true));
        }

        newSceneController.loadScene(this.stage, stage.getScene().getWidth(), stage.getScene().getHeight());
        currentSceneController = newSceneController;
    }

}
