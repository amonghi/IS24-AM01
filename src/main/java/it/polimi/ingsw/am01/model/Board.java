package it.polimi.ingsw.am01.model;


import java.util.Set;

public class Board {
    private Set<FaceUpCard> faceUpResourceCards;
    private Set<FaceUpCard> faceUpGoldenCards;
    private Deck resourceCardDeck;
    private Deck goldenCardDeck;

    public Board() {
        throw new UnsupportedOperationException("TODO");
    }

    public Set<Card> getFaceUpResourceCards() {
        throw new UnsupportedOperationException("TODO");
    }

    public Set<Card> getFaceUpGoldenCards() {
        throw new UnsupportedOperationException("TODO");
    }

    public Deck getResourceCardDeck() {
        throw new UnsupportedOperationException("TODO");
    }

    public Deck getGoldenCardDeck() {
        throw new UnsupportedOperationException("TODO");
    }
}
