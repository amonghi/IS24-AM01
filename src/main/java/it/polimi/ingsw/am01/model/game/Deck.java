package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;

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