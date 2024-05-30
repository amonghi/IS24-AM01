package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.component.RestoringPlayerSlotController;
import it.polimi.ingsw.am01.client.gui.event.SetPlayStatusEvent;
import it.polimi.ingsw.am01.network.message.c2s.ResumeGameC2S;
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
        gameLabel.setText("Restoring game #" + GUIView.getInstance().getGameId());}

    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                GUIView.getInstance().on(SetPlayStatusEvent.class, this::updatePlayerList)
        );
    }

    private void updatePlayerList(SetPlayStatusEvent event) {
        playerList.getChildren().clear();
        resumeButton.setDisable(event.players().stream().filter(GUIView.getInstance()::isConnected).count() <= 1);

        for (String player : event.players()) {
            playerList.getChildren().add(new RestoringPlayerSlotController(player, event.colors().get(player), event.connections().get(player)));
        }

    }


    @FXML
    private void resume() {
        GUIView.getInstance().sendMessage(
                new ResumeGameC2S()
        );
    }


    @Override
    public String getFXMLFileName() {
        return "restoring_lobby";
    }
}
