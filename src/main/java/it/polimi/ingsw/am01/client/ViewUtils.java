package it.polimi.ingsw.am01.client;

import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.model.card.CardColor;

/**
 * Utility class containing common methods for defining card file paths
 */
public class ViewUtils {
    //Back Ids
    public static final int RES_RED = 1;
    public static final int RES_GREEN = 11;
    public static final int RES_BLUE = 21;
    public static final int RES_PURPLE = 31;
    public static final int GOLD_RED = 41;
    public static final int GOLD_GREEN = 51;
    public static final int GOLD_BLUE = 66;
    public static final int GOLD_PURPLE = 73;

    /**
     * @param deckLocation The deck source, either {@link DeckLocation#RESOURCE_CARD_DECK} or {@link DeckLocation#GOLDEN_CARD_DECK}
     * @param color        The {@link CardColor} of the first card in the deck
     * @return The card id that corresponds to the stored image file path
     */
    public static int getIdFromColorAndDeckLocation(DeckLocation deckLocation, CardColor color) {
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

    /**
     * @param cardId The id of the selected card
     * @return The card id that corresponds to the stored image file path
     */
    public static int getFixedId(int cardId) {
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
}
