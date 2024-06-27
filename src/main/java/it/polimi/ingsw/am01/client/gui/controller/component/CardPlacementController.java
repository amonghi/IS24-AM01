package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.Position;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.Utils;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

/**
 * The controller for a card placement.
 * It contains the main methods to place the card in the correct position of the play area
 *
 * @see CardController
 */
public class CardPlacementController extends CardController implements Comparable<CardPlacementController> {

    private Position position;
    private int seq;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane card;

    /**
     * It constructs a new CardPlacementController, calling the constructor of {@link CardController}
     * It also calls the {@link ComponentController#loadComponent()} method
     *
     * @param id   The id of the card component
     * @param side The side of the card component
     * @see CardController
     */
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

    /**
     * @return The position of the card, in the i,j coordinate system
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * It sets the x and y coordinates of the card, corresponding to the place where it has to be placed,
     * based on the selected position
     *
     * @param i The i-coordinate of the position where the player wants to place the card
     * @param j The j-coordinate of the position where the player wants to place the card
     * @see Utils#computeXPosition(int, int)
     * @see Utils#computeYPosition(int, int)
     */
    public void setPosition(int i, int j) {
        this.position = new Position(i, j);
        card.setLayoutX(Utils.computeXPosition(position.i(), position.j()));
        card.setLayoutY(Utils.computeYPosition(position.i(), position.j()));

    }

    /**
     * It sets the sequence number of this placement
     *
     * @param seq The sequence number of this placement
     */
    public void setSeq(int seq) {
        this.seq = seq;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "card";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(CardPlacementController other) {
        return Integer.compare(this.seq, other.seq);
    }
}

