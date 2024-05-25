package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.component.PlayerSlotController;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class LobbyController extends SceneController {
    @FXML
    private HBox playerList;

    @FXML
    private Label gameId;

    @FXML
    private void initialize() {
        gameId.setText("In game #" + GUIView.getInstance().getGameId());
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().add(
                GUIView.getInstance().on(PlayerListChangedEvent.class, this::updatePlayerList)
        );
    }

    private void updatePlayerList(PlayerListChangedEvent event) {
        playerList.getChildren().clear();

        for (String player : event.playerList()) {
            playerList.getChildren().add(PlayerSlotController.of(player));
        }
        int maxPlayers = GUIView.getInstance().getMaxPlayers();
        for (int i = 0; i < maxPlayers - event.playerList().size(); i++) {
            playerList.getChildren().add(PlayerSlotController.empty());
        }
    }


    @Override
    public String getFXMLFileName() {
        return "lobby";
    }


    @FXML
    private void start() {

    }
}
