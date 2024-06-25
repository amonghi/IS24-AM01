package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.scene.PlayAreaController;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

/**
 * The controller for a face up card.
 * A face up card is a non-placeable card of which you can only see the front face
 *
 * @see CardController
 */
public class FaceUpSourceController extends CardController implements ComponentController {

    private final PlayAreaController playAreaController;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane card;

    /**
     * It constructs a new FaceUpSourceController, calling the constructor of {@link CardController} forcing the
     * side to be {@link Side#FRONT}, i.e. the visible face of the face up card on the play area
     * <p>
     * It also calls the {@link CardController#loadComponent()} method
     *
     * @param id                 The id of the face up card to draw
     * @param playAreaController The controller of the playarea, i.e. the scene where this component is used
     * @see CardController
     */
    public FaceUpSourceController(int id, PlayAreaController playAreaController) {
        super(id, Side.FRONT);
        this.playAreaController = playAreaController;
        loadComponent();
    }

    @FXML
    private void initialize() {
        String path = setFrontImage();
        card.setPrefWidth(Constants.BOARD_CARD_WIDTH);
        card.setPrefHeight(Constants.BOARD_CARD_HEIGHT);
        imageView.setFitWidth(Constants.BOARD_CARD_WIDTH);
        imageView.setFitHeight(Constants.BOARD_CARD_HEIGHT);
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(path)).toString(), Constants.BOARD_CARD_WIDTH, Constants.BOARD_CARD_HEIGHT, true, false));

        //Event handling
        imageView.setOnMouseClicked(event -> {
            playAreaController.drawFromFaceUp(super.cardId);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "card";
    }
}
