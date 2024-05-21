package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.network.message.c2s.JoinGameC2S;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class GameController extends AnchorPane implements ComponentController {

    private final int gameID;
    private final int maxPlayers;
    private final int currPlayers;

    @FXML
    private Label gameIDLabel;
    @FXML
    private Label playersLabel;
    @FXML
    private Button joinButton;

    public GameController(int gameID, int maxPlayers, int currPlayers) {
        this.gameID = gameID;
        this.maxPlayers = maxPlayers;
        this.currPlayers = currPlayers;

        loadComponent();
    }

    @FXML
    private void initialize() {
        gameIDLabel.setText("Game #" + gameID);
        playersLabel.setText(currPlayers + "/" + maxPlayers + " players");
    }

    @FXML
    private void join() {
        GUIView.getInstance().sendMessage(
                new JoinGameC2S(
                        gameID
                )
        );
    }

    @Override
    public String getFXMLFileName() {
        return "game";
    }
}