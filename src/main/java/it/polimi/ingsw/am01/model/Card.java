package it.polimi.ingsw.am01.model;

public class Card {
    private CardColor baseColor;
    private boolean isStarter;
    private boolean isGold;
    private FrontCardFace front;
    private BackCardFace back;

    public CardColor color() {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isStarter() {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isGold() {
        throw new UnsupportedOperationException("TODO");
    }

    public CardFace getFace(Side s) {
        throw new UnsupportedOperationException("TODO");
    }
}
