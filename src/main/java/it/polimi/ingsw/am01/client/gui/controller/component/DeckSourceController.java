package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.GUIView;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.model.card.CardColor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

import static it.polimi.ingsw.am01.client.gui.controller.Constants.*;

public class DeckSourceController extends AnchorPane implements ComponentController {

    private static final int SUFFIX_CARD_ID_LENGTH = 6;
    private CardColor color;
    private DeckLocation deckLocation;
    private int id;
    @FXML
    private ImageView imageView;
    @FXML
    private Button selectCard;

    public DeckSourceController(CardColor color, DeckLocation deckLocation) {
        this.color = color;
        this.deckLocation = deckLocation;
        this.id = getIdFromColorAndDeckLocation();
        loadComponent();
    }

    @FXML
    private void initialize() {
        String path = setImage();
        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(path)).toString()));
        initializeButtons();
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

    private void initializeButtons() {
        selectCard.setOnAction(event -> {
            GUIView.getInstance().getPlayAreaController().drawFromDeck(deckLocation);
        });
    }

    @Override
    public String getFXMLFileName() {
        return "deck";
    }
}
