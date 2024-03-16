package it.polimi.ingsw.am01.model.collectible;

import it.polimi.ingsw.am01.model.card.CardColor;

public enum Resource implements Collectible {
    PLANT,
    FUNGI,
    ANIMAL,
    INSECT;

    public CardColor getAssociatedColor() {
        throw new UnsupportedOperationException("TODO");
    }
}
