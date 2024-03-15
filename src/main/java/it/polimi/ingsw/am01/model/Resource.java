package it.polimi.ingsw.am01.model;

public enum Resource implements Collectible {
    PLANT,
    FUNGI,
    ANIMAL,
    INSECT;

    public CardColor getAssociatedColor() {
        throw new UnsupportedOperationException("TODO");
    }
}
