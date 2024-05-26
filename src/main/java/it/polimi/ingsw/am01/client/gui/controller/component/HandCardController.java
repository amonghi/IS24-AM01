package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class HandCardController extends CardController implements ComponentController {
    @FXML
    private ImageView imageView;
    @FXML
    private Button swapSide;
    @FXML
    private Button selectCard;

    public HandCardController(int id, Side side) {
        super(id, side);
        loadComponent();
    }

    @FXML
    private void initialize() {
        String path = setFrontImage();
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(path)).toString()));
        initializeButtons();
    }

    private void initializeButtons() {
        swapSide.setOnAction(this::swapSide);
        selectCard.setOnAction(this::selectCardToPlace);
    }

    private void selectCardToPlace(ActionEvent event) {
        GUIView.getInstance().getPlayAreaController().setCardPlacement(super.cardId, super.side);
    }

    private void swapSide(ActionEvent event) {
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
