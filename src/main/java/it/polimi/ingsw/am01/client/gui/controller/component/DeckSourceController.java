package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.model.card.CardColor;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

import static it.polimi.ingsw.am01.client.gui.controller.Constants.*;

public class DeckSourceController extends AnchorPane implements ComponentController {

    private static final int SUFFIX_CARD_ID_LENGTH = 6;
    private final CardColor color;
    private final DeckLocation deckLocation;
    private final int id;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane card;

    public DeckSourceController(CardColor color, DeckLocation deckLocation) {
        this.color = color;
        this.deckLocation = deckLocation;
        this.id = getIdFromColorAndDeckLocation();
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
            GUIView.getInstance().getPlayAreaController().drawFromDeck(this.deckLocation);
        });
    }

    private String setImage() {
        String formattedId = "0" + id + ".png";
        formattedId = formattedId.substring(formattedId.length() - SUFFIX_CARD_ID_LENGTH);
        return Constants.BACK_CARD_PATH + formattedId;
    }

    private int getIdFromColorAndDeckLocation() {
        int id = 0;
        switch (deckLocation) {
            case RESOURCE_CARD_DECK -> id = switch (color) {
                case RED:
                    yield RES_RED;
                case GREEN:
                    yield RES_GREEN;
                case BLUE:
                    yield RES_BLUE;
                case PURPLE:
                    yield RES_PURPLE;
                case NEUTRAL:
                    yield 0;
            };
            case GOLDEN_CARD_DECK -> id = switch (color) {
                case RED:
                    yield GOLD_RED;
                case GREEN:
                    yield GOLD_GREEN;
                case BLUE:
                    yield GOLD_BLUE;
                case PURPLE:
                    yield GOLD_PURPLE;
                case NEUTRAL:
                    yield 0;
            };
        }
        return id;
    }

    @Override
    public String getFXMLFileName() {
        return "card";
    }
}
