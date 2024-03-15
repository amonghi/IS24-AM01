package it.polimi.ingsw.am01.model;

import java.util.Set;

public class PatternObjective extends Objective {
    private CardColor[][] pattern;

    public PatternObjective(int points, CardColor[][] pattern) {
        super(points);
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Set<Set<CardPlacement>> test(PlayArea pa) {
        throw new UnsupportedOperationException("TODO");
    }
}
