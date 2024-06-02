package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class FaceUpSourceController extends CardController implements ComponentController {

    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane card;

    public FaceUpSourceController(int id) {
        super(id, Side.FRONT);
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
            GUIView.getInstance().getPlayAreaController().drawFromFaceUp(super.cardId);
        });
    }

    @Override
    public String getFXMLFileName() {
        return "card";
    }
}
