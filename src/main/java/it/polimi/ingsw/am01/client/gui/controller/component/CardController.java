package it.polimi.ingsw.am01.client.gui.controller.component;

import it.polimi.ingsw.am01.client.gui.controller.Constants;
import it.polimi.ingsw.am01.model.card.Side;
import javafx.scene.layout.AnchorPane;

import static it.polimi.ingsw.am01.client.gui.controller.Constants.*;
import static it.polimi.ingsw.am01.client.gui.controller.Constants.GOLD_PURPLE;

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
        String formattedId = "0" + getFixedId() + ".png";
        formattedId = formattedId.substring(formattedId.length() - SUFFIX_CARD_ID_LENGTH);
        return Constants.BACK_CARD_PATH + formattedId;
    }

    public int getFixedId() {
        if (cardId >= 1 && cardId <= 10)
            return RES_RED;
        else if (cardId >= 11 && cardId <= 20)
            return RES_GREEN;
        else if (cardId >= 21 && cardId <= 30)
            return RES_BLUE;
        else if (cardId >= 31 && cardId <= 40)
            return RES_PURPLE;
        else if (cardId >= 41 && cardId <= 50)
            return GOLD_RED;
        else if (cardId >= 51 && cardId <= 60)
            return GOLD_GREEN;
        else if (cardId >= 61 && cardId <= 70)
            return GOLD_BLUE;
        else if (cardId >= 71 && cardId <= 80)
            return GOLD_PURPLE;
        else if (cardId >= 81 && cardId <= 86)
            return cardId;
        return 0;
    }

    public int getCardId() {
        return this.cardId;
    }

    public Side getSide() {
        return this.side;
    }
}
