package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.ViewUtils;
import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.scene.layout.AnchorPane;

/**
 * The base controller for a card.
 * It has methods for managing the common aspects of all cards, i.e. setting the image and size
 */
public abstract class CardController extends AnchorPane implements ComponentController {

    private static final int SUFFIX_CARD_ID_LENGTH = 6;
    protected int cardId;
    protected Side side;

    /**
     * Constructs a new CardController
     *
     * @param cardId The id of the card component
     * @param side   The side of the card component
     */
    public CardController(int cardId, Side side) {
        this.cardId = cardId;
        this.side = side;
    }

    /**
     * @return The path of the image of the card's front face
     */
    public String setFrontImage() {
        String formattedId = "0" + cardId + ".png";
        formattedId = formattedId.substring(formattedId.length() - SUFFIX_CARD_ID_LENGTH);
        return Constants.FRONT_CARD_PATH + formattedId;
    }

    /**
     * @return The path of the image of the card's back face
     */
    public String setBackImage() {
        String formattedId = "0" + ViewUtils.getFixedId(cardId) + ".png";
        formattedId = formattedId.substring(formattedId.length() - SUFFIX_CARD_ID_LENGTH);
        return Constants.BACK_CARD_PATH + formattedId;
    }

    /**
     * It sets the width of the card component
     *
     * @param width The width of the card
     */
    public void setWidth(int width) {
        this.setPrefWidth(width);
        this.setMaxWidth(USE_PREF_SIZE);
        this.setMinWidth(USE_PREF_SIZE);
    }

    /**
     * It sets the height of the card component
     *
     * @param height The height of the card
     */
    public void setHeight(int height) {
        this.setPrefHeight(height);
        this.setMaxHeight(USE_PREF_SIZE);
        this.setMinHeight(USE_PREF_SIZE);
    }

    /**
     * @return The id of the controlled card
     */
    public int getCardId() {
        return this.cardId;
    }

    /**
     * @return The {@link Side} of the controlled card
     */
    public Side getSide() {
        return this.side;
    }
}
