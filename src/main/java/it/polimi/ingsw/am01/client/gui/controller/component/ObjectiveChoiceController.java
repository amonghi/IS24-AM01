package it.polimi.ingsw.am01.client.gui.controller.component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * The controller for the component used to show if a player has chosen his or her
 * {@link it.polimi.ingsw.am01.model.objective.Objective}
 *
 * @see it.polimi.ingsw.am01.model.objective.Objective
 * @see it.polimi.ingsw.am01.client.gui.controller.scene.SelectObjectiveController
 */
public class ObjectiveChoiceController extends VBox implements ComponentController {

    private final String playerName;
    @FXML
    private Label playerNameLabel;
    @FXML
    private ImageView selectedIcon;

    /**
     * It constructs a new ObjectiveChoiceController.
     * It also calls the {@link ComponentController#loadComponent()} method
     *
     * @param playerName The name of the player who has chosen his or her secret objective
     */
    public ObjectiveChoiceController(String playerName) {
        this.playerName = playerName;

        loadComponent();
    }

    @FXML
    private void initialize() {
        selectedIcon.setVisible(false);
        playerNameLabel.setText(playerName);
    }

    /**
     * It shows whether a player has chosen his or her secret objective
     */
    public void setChoice() {
        selectedIcon.setVisible(true);
    }

    /**
     * @return The name of the player who has chosen his or her secret objective
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "objective_choice";
    }
}
