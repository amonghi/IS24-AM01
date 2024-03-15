package it.polimi.ingsw.am01.model;

import java.util.Set;

public abstract class Objective {
    private int points;

    public Objective(int points) {
        throw new UnsupportedOperationException("TODO");
    }

    public int getPointsPerMatch() {
        throw new UnsupportedOperationException("TODO");
    }

    public abstract Set<Set<CardPlacement>> test(PlayArea pa);
}
