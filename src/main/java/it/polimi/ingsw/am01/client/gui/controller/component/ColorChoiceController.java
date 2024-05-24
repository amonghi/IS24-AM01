package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class ColorChoiceController extends AnchorPane implements ComponentController {

    private final String playerName;
    @FXML
    private Label playerNameLabel;
    @FXML
    private Circle playerColorCircle;

    public ColorChoiceController(String playerName) {
        this.playerName = playerName;

        loadComponent();
    }

    @FXML
    private void initialize() {
        playerNameLabel.setText(playerName + ": . . .");
    }

    public void setChoice(PlayerColor playerColor) { //TODO: maybe create inner enum?
        playerColorCircle.setFill(
                switch (playerColor) {
                    case BLUE -> javafx.scene.paint.Color.BLUE;
                    case RED -> javafx.scene.paint.Color.RED;
                    case YELLOW -> javafx.scene.paint.Color.YELLOW;
                    case GREEN -> javafx.scene.paint.Color.GREEN;
                });
        playerColorCircle.setVisible(true);
        playerNameLabel.setText(playerName + ":");
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String getFXMLFileName() {
        return "player_color_choice";
    }
}
