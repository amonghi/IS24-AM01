package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class FaceUpSourceController extends CardController implements ComponentController {

    @FXML
    private ImageView imageView;
    @FXML
    private Button selectCard;
    @FXML
    private Button swapSide;

    public FaceUpSourceController(int id) {
        super(id, Side.FRONT);
        loadComponent();
    }

    @FXML
    private void initialize() {
        String path = setFrontImage();
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(path)).toString()));
        initializeButtons();
    }

    private void initializeButtons() {
        swapSide.setVisible(false);
        swapSide.setDisable(true);
        selectCard.setOnAction(event -> {
            GUIView.getInstance().getPlayAreaController().drawFromFaceUp(super.cardId);
        });
    }

    @Override
    public String getFXMLFileName() {
        return "card";
    }
}
