package it.polimi.ingsw.am01.client.gui.controller.scene;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.component.PlayerSlotController;
import it.polimi.ingsw.am01.client.gui.event.InvalidStartGameRequestEvent;
import it.polimi.ingsw.am01.client.gui.event.PlayerListChangedEvent;
import it.polimi.ingsw.am01.network.message.c2s.StartGameC2S;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.List;

public class LobbyController extends SceneController {
    @FXML
    private HBox playerList;

    @FXML
    private Label gameId;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        gameId.setText("In game #" + GUIView.getInstance().getGameId());
    }

    @Override
    protected void registerListeners() {
        getViewRegistrations().addAll(List.of(
                GUIView.getInstance().on(PlayerListChangedEvent.class, this::updatePlayerList),
                GUIView.getInstance().on(InvalidStartGameRequestEvent.class, this::invalidStart)
        ));
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

    private void invalidStart(InvalidStartGameRequestEvent event) {
        messageLabel.setVisible(true);
    }

    @Override
    public String getFXMLFileName() {
        return "lobby";
    }


    @FXML
    private void start() {
        GUIView.getInstance().sendMessage(
                new StartGameC2S()
        );
    }
}
