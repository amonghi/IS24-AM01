package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Utils;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class RestoringPlayerSlotController extends VBox implements ComponentController {
    private final String player;
    private final PlayerColor playerColor;
    private final Boolean isConnected;
    @FXML
    Circle pawn;

    @FXML
    Label playerLabel;

    @FXML
    Label connectionLabel;


    @FXML
    private void initialize() {
        playerLabel.setText(player);
        pawn.setFill(Utils.convertColor(View.getInstance().getPlayerColor(player)));
        if(isConnected){
            connectionLabel.setText("connected");
            connectionLabel.setTextFill(Color.GREEN);
        } else {
            connectionLabel.setText("disconnected");
            connectionLabel.setTextFill(Color.RED);
        }
    }



    public RestoringPlayerSlotController(String player, PlayerColor playerColor, Boolean isConnected) {
        this.player = player;
        this.playerColor = playerColor;
        this.isConnected = isConnected;

        loadComponent();
    }

    @Override
    public String getFXMLFileName() {
        return "restoring_player_slot";
    }
}
