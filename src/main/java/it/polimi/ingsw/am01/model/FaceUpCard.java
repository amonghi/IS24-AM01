package it.polimi.ingsw.am01.model;

import java.util.Optional;

public class FaceUpCard implements DrawSource {
    private Optional<Card> card;
    private Deck source;

    public FaceUpCard() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Optional<Card> draw() {
        throw new UnsupportedOperationException("TODO");
    }

    public Optional<Card> getCard() {
        throw new UnsupportedOperationException("TODO");
    }
}
