package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.ViewUtils;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.client.gui.controller.scene.PlayAreaController;
import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.model.card.CardColor;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

import static it.polimi.ingsw.am01.client.gui.controller.Constants.BOARD_CARD_HEIGHT;
import static it.polimi.ingsw.am01.client.gui.controller.Constants.BOARD_CARD_WIDTH;

/**
 * The controller for a deck source
 *
 * @see ComponentController
 */
public class DeckSourceController extends AnchorPane implements ComponentController {

    private static final int SUFFIX_CARD_ID_LENGTH = 6;
    private final CardColor color;
    private final DeckLocation deckLocation;
    private final PlayAreaController playAreaController;
    private final int id;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane card;

    /**
     * It constructs a new DeckSourceController.
     * It also calls the {@link CardController#loadComponent()} method
     *
     * @param color              The color of the top card, i.e. the one to draw
     * @param deckLocation       The type of the deck
     * @param playAreaController The controller of the play area, i.e. the scene where this component is used
     */
    public DeckSourceController(CardColor color, DeckLocation deckLocation, PlayAreaController playAreaController) {
        this.color = color;
        this.deckLocation = deckLocation;
        this.playAreaController = playAreaController;
        this.id = ViewUtils.getIdFromColorAndDeckLocation(deckLocation, color);
        loadComponent();
    }

    @FXML
    private void initialize() {
        String path = setImage();
        card.setPrefWidth(BOARD_CARD_WIDTH);
        card.setPrefHeight(BOARD_CARD_HEIGHT);
        imageView.setFitWidth(BOARD_CARD_WIDTH);
        imageView.setFitHeight(BOARD_CARD_HEIGHT);
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(path)).toString(), BOARD_CARD_WIDTH, BOARD_CARD_HEIGHT, true, false));

        //Event handling
        imageView.setOnMouseClicked(event -> {
            playAreaController.drawFromDeck(this.deckLocation);
        });
    }

    /**
     * @return The path of the image of the card's back face, i.e. the card that can be drawn from the deck
     */
    private String setImage() {
        String formattedId = "0" + id + ".png";
        formattedId = formattedId.substring(formattedId.length() - SUFFIX_CARD_ID_LENGTH);
        return Constants.BACK_CARD_PATH + formattedId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFXMLFileName() {
        return "card";
    }
}
