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
    private final View view;
    @FXML
    Circle pawn;
    @FXML
    Label playerLabel;
    @FXML
    Label connectionLabel;


    public RestoringPlayerSlotController(String player, PlayerColor playerColor, Boolean isConnected, View view) {
        this.view = view;
        this.player = player;
        this.playerColor = playerColor;
        this.isConnected = isConnected;

        loadComponent();
    }

    @FXML
    private void initialize() {
        playerLabel.setText(player);
        pawn.setFill(Utils.convertColor(view.getPlayerColor(player)));
        if (isConnected) {
            connectionLabel.setText("connected");
            connectionLabel.setTextFill(Color.GREEN);
        } else {
            connectionLabel.setText("disconnected");
            connectionLabel.setTextFill(Color.RED);
        }
    }

    @Override
    public String getFXMLFileName() {
        return "restoring_player_slot";
    }
}
