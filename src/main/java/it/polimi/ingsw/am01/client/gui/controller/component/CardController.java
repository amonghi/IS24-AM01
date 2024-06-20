package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.client.ViewUtils;
import javafx.scene.layout.AnchorPane;

public abstract class CardController extends AnchorPane implements ComponentController {

    protected int cardId;
    protected Side side;
    private static final int SUFFIX_CARD_ID_LENGTH = 6;

    public CardController(int cardId, Side side) {
        this.cardId = cardId;
        this.side = side;
    }

    public String setFrontImage() {
        String formattedId = "0" + cardId + ".png";
        formattedId = formattedId.substring(formattedId.length() - SUFFIX_CARD_ID_LENGTH);
        return Constants.FRONT_CARD_PATH + formattedId;
    }

    public String setBackImage() {
        String formattedId = "0" + ViewUtils.getFixedId(cardId) + ".png";
        formattedId = formattedId.substring(formattedId.length() - SUFFIX_CARD_ID_LENGTH);
        return Constants.BACK_CARD_PATH + formattedId;
    }


    //TODO: unify card and card_placement fxml file and use these methods to set dimensions
    public void setWidth(int width) {
        this.setPrefWidth(width);
        this.setMaxWidth(USE_PREF_SIZE);
        this.setMinWidth(USE_PREF_SIZE);
    }

    public void setHeight(int height) {
        this.setPrefHeight(height);
        this.setMaxHeight(USE_PREF_SIZE);
        this.setMinHeight(USE_PREF_SIZE);
    }

    public int getCardId() {
        return this.cardId;
    }

    public Side getSide() {
        return this.side;
    }
}
