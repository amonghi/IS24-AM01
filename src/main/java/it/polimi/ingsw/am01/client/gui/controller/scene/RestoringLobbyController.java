package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.component.RestoringPlayerSlotController;
import it.polimi.ingsw.am01.client.gui.event.SetPlayStatusEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class RestoringLobbyController extends SceneController {

    @FXML
    Label gameLabel;

    @FXML
    HBox playerList;

    @FXML
    Button resumeButton;

    @FXML
    private void initialize() {
        gameLabel.setText("Restoring game #" + View.getInstance().getGameId());
        registerListeners();
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                View.getInstance().on(SetPlayStatusEvent.class, this::updatePlayerList)
        );
    }

    private void updatePlayerList(SetPlayStatusEvent event) {
        Platform.runLater(() -> {
            playerList.getChildren().clear();
            resumeButton.setDisable(event.players().stream().filter(View.getInstance()::isConnected).count() <= 1);

            for (String player : event.players()) {
                playerList.getChildren().add(new RestoringPlayerSlotController(player, event.colors().get(player), event.connections().get(player)));
            }
        });
    }

    @FXML
    private void resume() {
        View.getInstance().resumeGame();
    }

    @Override
    public String getFXMLFileName() {
        return "restoring_lobby";
    }
}
