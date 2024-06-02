package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.Utils;
import it.polimi.ingsw.am01.client.gui.model.Position;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class CardPlacementController extends CardController implements Comparable<CardPlacementController> {

    private Position position;
    private int seq;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane card;

    public CardPlacementController(int id, Side side) {
        super(id, side);
        loadComponent();
    }

    @FXML
    private void initialize() {
        String path = side.equals(Side.FRONT) ? setFrontImage() : setBackImage();
        card.setPrefWidth(Constants.CARD_PLACEMENT_WIDTH);
        card.setPrefHeight(Constants.CARD_PLACEMENT_HEIGHT);
        imageView.setFitWidth(Constants.CARD_PLACEMENT_WIDTH);
        imageView.setFitHeight(Constants.CARD_PLACEMENT_HEIGHT);
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(path)).toString(), Constants.CARD_PLACEMENT_WIDTH, Constants.CARD_PLACEMENT_HEIGHT, true, false));
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(int i, int j) {
        this.position = new Position(i, j);
        card.setLayoutX(Utils.computeXPosition(position.i(), position.j()));
        card.setLayoutY(Utils.computeYPosition(position.i(), position.j()));

    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public String getFXMLFileName() {
        return "card";
    }

    @Override
    public int compareTo(CardPlacementController other) {
        return Integer.compare(this.seq, other.seq);
    }
}

