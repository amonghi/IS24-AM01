package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.View;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.Utils;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.util.Map;
import java.util.Objects;

public class EndingPlayerController extends AnchorPane implements ComponentController {
    private final String player;
    private final int points;
    private final int placement;
    private final Map<String, PlayerColor> playerColors;
    @FXML
    Circle pawn;

    @FXML
    ImageView crown;

    @FXML
    Label playerLabel;

    @FXML
    Label pointsLabel;

    @FXML
    Label placementLabel;

    @FXML
    private void initialize() {
        playerLabel.setText(player);
        placementLabel.setText(placement + ".");
        pointsLabel.setText(points + " points");
        pawn.setFill(Utils.convertColor(this.playerColors.get(player)));
        if (placement == 1) {
            crown.setImage(new Image(Objects.requireNonNull(getClass().getResource(Constants.ICONS_PATH + "crown.png")).toString()));
        }
    }

    public EndingPlayerController(String player, int points, int placement, Map<String, PlayerColor> playerColors) {
        this.player = player;
        this.points = points;
        this.placement = placement;
        this.playerColors = playerColors;

        loadComponent();
    }


    @Override
    public String getFXMLFileName() {
        return "ending_player";
    }
}
