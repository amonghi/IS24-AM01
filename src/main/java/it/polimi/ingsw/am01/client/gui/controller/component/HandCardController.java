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

public class HandCardController extends CardController implements ComponentController {
    private final PlayAreaController playAreaController;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane card;

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

    private void selectCardToPlace() {
        playAreaController.setCardPlacement(super.cardId, super.side);
    }

    private void swapSide(MouseEvent event) {
        super.side = super.side.equals(Side.FRONT) ? Side.BACK : Side.FRONT;
        imageView.getImage().cancel();
        String path = super.side.equals(Side.BACK) ? setBackImage() : setFrontImage();
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(path)).toString()));
    }

    @Override
    public String getFXMLFileName() {
        return "card";
    }
}
