package it.polimi.ingsw.am01.client.gui.controller.component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PlayerSlotController extends VBox implements ComponentController {
    @FXML
    private final String playerName;

    @FXML
    private Label playerLabel;

    private PlayerSlotController(String playerName){
        this.playerName = playerName;

        loadComponent();
    }

    public static PlayerSlotController of(String playerName){
        return new PlayerSlotController(playerName);
    }

    public static PlayerSlotController empty(){
        return new PlayerSlotController("");
    }

    @FXML
    private void initialize() {
        playerLabel.setText(playerName);
    }

    @Override
    public String getFXMLFileName() {
        return "player_slot";
    }
}
