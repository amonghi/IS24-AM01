package it.polimi.ingsw.am01.client.gui.controller.component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ObjectiveChoiceController extends VBox implements ComponentController {

    private final String playerName;
    @FXML
    private Label playerNameLabel;
    @FXML
    private ImageView selectedIcon;

    public ObjectiveChoiceController(String playerName) {
        this.playerName = playerName;

        loadComponent();
    }

    @FXML
    private void initialize() {
        selectedIcon.setVisible(false);
        playerNameLabel.setText(playerName);
    }

    public void setChoice() {
        selectedIcon.setVisible(true);
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String getFXMLFileName() {
        return "objective_choice";
    }
}
