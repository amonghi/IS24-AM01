package it.polimi.ingsw.am01.client.gui.controller.component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * The controller for the component that show a player in the lobby, waiting for the others players
 */
public class PlayerSlotController extends VBox implements ComponentController {
    @FXML
    private final String playerName;
    @FXML
    private Label playerLabel;

    /**
     * It constructs a new PlayerSlotController.
     * It also calls the {@link ComponentController#loadComponent()} method
     *
     * @param playerName The name of the player to be shown
     */
    private PlayerSlotController(String playerName) {
        this.playerName = playerName;
        loadComponent();
    }

    /**
     * It calls the constructor, with the name of the player who has just joined the game
     *
     * @param playerName The name of the player to be shown
     * @return The {@link PlayerSlotController} for the specified player
     */
    public static PlayerSlotController of(String playerName) {
        return new PlayerSlotController(playerName);
    }

    /**
     * It calls the constructor, with an empty name
     *
     * @return The {@link PlayerSlotController} of a missing player, i.e. a player who has not yet joined the game
     */
    public static PlayerSlotController empty() {
        return new PlayerSlotController("");
    }

    @FXML
    private void initialize() {
        playerLabel.setText(playerName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "player_slot";
    }
}
