package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.scene.PlayAreaController;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

/**
 * The controller for a hand card.
 * A hand card is a placeable card of which you can see both the front and the back face
 *
 * @see CardController
 */
public class HandCardController extends CardController implements ComponentController {
    private final PlayAreaController playAreaController;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane card;

    /**
     * It constructs a new HandCardController, calling the constructor of {@link CardController}
     * <p>
     * It also calls the {@link CardController#loadComponent()} method
     *
     * @param id                 The id of the card to place
     * @param side               The side of the card to place
     * @param playAreaController The controller of the play area, i.e. the scene where this component is used
     * @see CardController
     */
    public HandCardController(int id, Side side, PlayAreaController playAreaController) {
        super(id, side);
        this.playAreaController = playAreaController;
        loadComponent();
    }

    @FXML
    private void initialize() {
        String path = setFrontImage();
        card.setPrefWidth(Constants.HAND_CARD_WIDTH);
        card.setPrefHeight(Constants.HAND_CARD_HEIGHT);
        imageView.setFitWidth(Constants.HAND_CARD_WIDTH);
        imageView.setFitHeight(Constants.HAND_CARD_HEIGHT);
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(path)).toString(), Constants.HAND_CARD_WIDTH, Constants.HAND_CARD_HEIGHT, true, false));

        //Event handling
        imageView.setOnMouseClicked(this::swapSide);
        imageView.setOnDragDetected(event -> {
            selectCardToPlace();
            Dragboard dragboard = imageView.startDragAndDrop(TransferMode.ANY);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putImage(new Image(imageView.getImage().getUrl(), Constants.CARD_PLACEMENT_WIDTH, Constants.CARD_PLACEMENT_HEIGHT, true, false));
            dragboard.setContent(clipboardContent);
        });
        imageView.setOnDragExited(event -> {
            playAreaController.clearPositionLayer();
        });
    }

    /**
     * It calls the {@link PlayAreaController#setCardPlacement(int, Side)} method
     */
    private void selectCardToPlace() {
        playAreaController.setCardPlacement(super.cardId, super.side);
    }

    /**
     * It flips the card showing:
     * <ul>
     *     <li> The back face, if the front is currently visible </li>
     *     <li> The front face, if the back is currently visible </li>
     * </ul>
     *
     * @param event The {@link MouseEvent} generated when the hand card is clicked
     */
    private void swapSide(MouseEvent event) {
        super.side = super.side.equals(Side.FRONT) ? Side.BACK : Side.FRONT;
        imageView.getImage().cancel();
        String path = super.side.equals(Side.BACK) ? setBackImage() : setFrontImage();
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(path)).toString()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "card";
    }
}
