package it.polimi.ingsw.am01.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class PlayArea implements Iterable<CardPlacement> {
    private Map<Position, CardPlacement> cards;
    private int score;
    private int seq;
    private Map<Collectible, Integer> collectibleCount;

    public PlayArea(Card starterCard, Side side) {
        throw new UnsupportedOperationException("TODO");
    }

    public CardPlacement placeAt(int i, int j, Card c, Side side) {
        throw new UnsupportedOperationException("TODO");
    }

    public Optional<CardPlacement> getAt(int i, int j) {
        throw new UnsupportedOperationException("TODO");
    }

    public Map<Collectible, Integer> getCollectibleCount() {
        throw new UnsupportedOperationException("TODO");
    }

    public int getScore() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Iterator<CardPlacement> iterator() {
        throw new UnsupportedOperationException("TODO");
    }

    public WindowIterator windows(int width, int height) {
        throw new UnsupportedOperationException("TODO");
    }
}
