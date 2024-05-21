package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LobbyController extends SceneController {
    @FXML
    private VBox playerList;


    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                GUIView.getInstance().on(PlayerListChangedEvent.class, this::updatePlayerList)
        );
    }

    private void updatePlayerList(PlayerListChangedEvent event) {
        playerList.getChildren().clear();
        for(String player : event.playerList()){
            playerList.getChildren().add(new Label(player));
        }
    }


    @Override
    public String getFXMLFileName() {
        return "lobby";
    }
}
