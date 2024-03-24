package it.polimi.ingsw.am01.model.card;

import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.CardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;

/**
 * Card represents placeable card of the game (which are allocated into PlayArea). In particular: resource, gold and starter card
 */
public class Card {
    private final int id;
    private final CardColor baseColor;
    private final boolean isStarter;
    private final boolean isGold;
    private final FrontCardFace front;
    private final BackCardFace back;

    /**
     * Constructs a new Card and sets all parameters specified
     * @param id The id of card
     * @param baseColor The color of card
     * @param isStarter A boolean that specified if card is starter or not
     * @param isGold A boolean that specified if card is golden or not
     * @param front The front face of card
     * @param back The back face of card
     */
    public Card(int id, CardColor baseColor, boolean isStarter, boolean isGold, FrontCardFace front, BackCardFace back) {
        this.id = id;
        this.baseColor = baseColor;
        this.isStarter = isStarter;
        this.isGold = isGold;
        this.front = front;
        this.back = back;
    }
    /**
     * Provides id of the card
     * @return Returns the id of the card
     */
    public int id() {
        return id;
    }

    /**
     * Provides color of the card. Starter card has NEUTRAL color
     * @return Returns the color of the card
     */
    public CardColor color() {
        return baseColor;
    }

    /**
     * Establish if card is starter or not
     * @return Returns true if card is a starter, otherwise false
     */
    public boolean isStarter() {
        return isStarter;
    }

    /**
     * Establish if card is a gold card or resource card
     * @return Returns true if card is gold, otherwise false
     */
    public boolean isGold() {
        return isGold;
    }

    /**
     * Provides specific face of the card
     * @param s A reference to the side of the card
     * @return Returns the CardFace of the required side
     */
    public CardFace getFace(Side s) {
        return switch (s) {
            case FRONT -> front;
            case BACK -> back;
        };
    }
}