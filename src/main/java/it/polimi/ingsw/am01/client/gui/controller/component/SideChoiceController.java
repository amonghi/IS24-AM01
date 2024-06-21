package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Objects;


public class SideChoiceController extends VBox implements ComponentController {

    private final String playerName;
    @FXML
    private Label playerNameLabel;
    @FXML
    private ImageView cardImage;

    public SideChoiceController(String playerName) {
        this.playerName = playerName;

        loadComponent();
    }

    @FXML
    private void initialize() {
        playerNameLabel.setText(playerName);
    }

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

    public String getPlayerName() {
        return playerName;
    }


    @Override
    public String getFXMLFileName() {
        return "side_choice";
    }
}
