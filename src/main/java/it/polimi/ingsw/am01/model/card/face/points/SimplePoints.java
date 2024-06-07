package it.polimi.ingsw.am01.model.card.face.points;

import it.polimi.ingsw.am01.model.game.PlayArea;

import java.util.Objects;

/**
 * SimplePoints is type of Points that gives a constant score for a card placement
 */
public final class SimplePoints implements Points {
    private final int points;

    /**
     * Constructs a new SimplePoints
     * @param points The constant number of point gained after the card placement
     */
    public SimplePoints(int points) {
        this.points = points;
    }

    /**
     * @return the number of points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param cp A reference to the card placement
     * @return Returns the constant points value associated to the card
     */
    @Override
    public int calculateScoredPoints(PlayArea.CardPlacement cp) {
        return points;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePoints that = (SimplePoints) o;
        return points == that.points;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(points);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SimplePoints{" +
                "points=" + points +
                '}';
    }
}