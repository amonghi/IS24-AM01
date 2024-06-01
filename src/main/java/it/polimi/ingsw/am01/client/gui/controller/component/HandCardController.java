package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

import java.util.Objects;

public class HandCardController extends CardController implements ComponentController {
    @FXML
    private ImageView imageView;

    public HandCardController(int id, Side side) {
        super(id, side);
        loadComponent();
    }

    @FXML
    private void initialize() {
        String path = setFrontImage();
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(path)).toString()));

        //Event handling
        imageView.setOnMouseClicked(this::swapSide);
        imageView.setOnDragDetected(event -> {
            selectCardToPlace();
            Dragboard dragboard = imageView.startDragAndDrop(TransferMode.ANY);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putImage(imageView.getImage());
            dragboard.setContent(clipboardContent);
        });
        imageView.setOnDragExited(event -> {
            GUIView.getInstance().getPlayAreaController().clearPositionLayer();
        });
    }

    private void selectCardToPlace() {
        GUIView.getInstance().getPlayAreaController().setCardPlacement(super.cardId, super.side);
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
