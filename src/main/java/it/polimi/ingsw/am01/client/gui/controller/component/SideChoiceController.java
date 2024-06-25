package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Objects;

/**
 * The controller for the component used to show if a player has chosen his or her
 * starting card {@link Side}
 *
 * @see it.polimi.ingsw.am01.client.gui.controller.scene.SelectStartingCardSideController
 */
public class SideChoiceController extends VBox implements ComponentController {

    private final String playerName;
    @FXML
    private Label playerNameLabel;
    @FXML
    private ImageView cardImage;

    /**
     * It constructs a new SideChoiceController.
     * It also calls the {@link CardController#loadComponent()} method
     *
     * @param playerName The name of the player who has chosen his or her starting card side
     */
    public SideChoiceController(String playerName) {
        this.playerName = playerName;

        loadComponent();
    }

    @FXML
    private void initialize() {
        playerNameLabel.setText(playerName);
    }

    /**
     * It shows the name of the player along with the chosen starting card side, setting its image
     *
     * @param cardId The id of the card
     * @param side   The chosen side
     * @see it.polimi.ingsw.am01.client.gui.controller.scene.SelectStartingCardSideController
     */
    public void setChoice(int cardId, Side side) {
        String path = null;
        switch (side) {
            case FRONT -> {
                if (cardId < 10) {
                    path = Constants.FRONT_CARD_PATH + "0" + cardId + Constants.IMAGE_EXTENSION;
                } else {
                    path = Constants.FRONT_CARD_PATH + cardId + Constants.IMAGE_EXTENSION;
                }
            }
            case BACK -> {
                if (cardId < 10) {
                    path = Constants.BACK_CARD_PATH + "0" + cardId + Constants.IMAGE_EXTENSION;
                } else {
                    path = Constants.BACK_CARD_PATH + cardId + Constants.IMAGE_EXTENSION;
                }
            }
        }
        cardImage.setImage(new Image(Objects.requireNonNull(SideChoiceController.class.getResource(path)).toString()));
        cardImage.setVisible(true);
        playerNameLabel.setText(playerName);
    }

    /**
     * @return The name of the player who has chosen his or her starting card side
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "side_choice";
    }
}
