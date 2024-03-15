package it.polimi.ingsw.am01.model;

import java.util.Optional;

public class Window {
    private PlayArea playArea;
    private int offsetI;
    private int offsetJ;
    private int width;
    private int height;

    public int width() {
        throw new UnsupportedOperationException("TODO");
    }

    public int height() {
        throw new UnsupportedOperationException("TODO");
    }

    public Optional<CardPlacement> getAt(int i, int j) {
        throw new UnsupportedOperationException("TODO");
    }
}
