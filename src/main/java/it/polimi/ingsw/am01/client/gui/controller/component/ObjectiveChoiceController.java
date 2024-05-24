package it.polimi.ingsw.am01.client.gui.controller.component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ObjectiveChoiceController extends AnchorPane implements ComponentController {

    private final String playerName;
    @FXML
    private Label playerNameLabel;

    public ObjectiveChoiceController(String playerName) {
        this.playerName = playerName;

        loadComponent();
    }

    @FXML
    private void initialize() {
        playerNameLabel.setText(playerName + ": . . .");

    }

    public void setChoice() {
        playerNameLabel.setText(playerName + ": chosen");
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String getFXMLFileName() {
        return "objective_choice";
    }
}
