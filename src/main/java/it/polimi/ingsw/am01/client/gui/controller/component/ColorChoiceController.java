package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.model.player.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

/**
 * The controller for the component used to show the {@link PlayerColor}
 * chosen by the players
 *
 * @see PlayerColor
 * @see it.polimi.ingsw.am01.client.gui.controller.scene.SelectPlayerColorController
 */
public class ColorChoiceController extends VBox implements ComponentController {

    private final String playerName;
    @FXML
    private Label playerNameLabel;
    @FXML
    private Circle playerColorCircle;

    /**
     * It constructs a new ColorChoiceController.
     * It also calls the {@link CardController#loadComponent()} method
     *
     * @param playerName The name of the player who has chosen his or her color
     */
    public ColorChoiceController(String playerName) {
        this.playerName = playerName;

        loadComponent();
    }

    @FXML
    private void initialize() {
        playerNameLabel.setText(playerName);
    }

    /**
     * It shows the name of the player along with the chosen color
     *
     * @param playerColor The color the player has just chosen
     * @see it.polimi.ingsw.am01.client.gui.controller.scene.SelectPlayerColorController
     */
    public void setChoice(PlayerColor playerColor) {
        playerColorCircle.setFill(
                switch (playerColor) {
                    case BLUE -> javafx.scene.paint.Color.BLUE;
                    case RED -> javafx.scene.paint.Color.RED;
                    case YELLOW -> javafx.scene.paint.Color.YELLOW;
                    case GREEN -> javafx.scene.paint.Color.GREEN;
                });
        playerColorCircle.setVisible(true);
        playerNameLabel.setText(playerName);
    }

    /**
     * @return The name of the player who has chosen his or her color
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "player_color_choice";
    }
}
