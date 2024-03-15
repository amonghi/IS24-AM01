package it.polimi.ingsw.am01.model;

import java.util.List;
import java.util.Optional;

public class Deck implements DrawSource {
    private List<Card> cards;

    public Deck(Iterable<Card> cards) {
        throw new UnsupportedOperationException("TODO");
    }

    public void shuffle() {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("TODO");
    }

    public Optional<Card> draw() {
        throw new UnsupportedOperationException("TODO");
    }
}