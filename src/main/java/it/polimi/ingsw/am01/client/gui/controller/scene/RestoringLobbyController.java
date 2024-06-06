package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.component.RestoringPlayerSlotController;
import it.polimi.ingsw.am01.client.gui.event.SetPlayStatusEvent;
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

    public RestoringLobbyController(View view) {
        super(view);
    }

    @FXML
    private void initialize() {
        gameLabel.setText("Restoring game #" + view.getGameId());
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                view.on(SetPlayStatusEvent.class, this::updatePlayerList)
        );
    }

    private void updatePlayerList(SetPlayStatusEvent event) {
        playerList.getChildren().clear();
        resumeButton.setDisable(event.players().stream().filter(view::isConnected).count() <= 1);

        for (String player : event.players()) {
            playerList.getChildren().add(new RestoringPlayerSlotController(
                    player,
                    event.colors().get(player),
                    event.connections().get(player),
                    view
            ));
        }
    }

    @FXML
    private void resume() {
        view.resumeGame();
    }

    @Override
    public String getFXMLFileName() {
        return "restoring_lobby";
    }
}
