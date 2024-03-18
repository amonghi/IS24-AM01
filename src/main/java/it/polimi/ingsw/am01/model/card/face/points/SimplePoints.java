package it.polimi.ingsw.am01.model.card.face.points;

import it.polimi.ingsw.am01.model.game.PlayArea;

/**
 * SimplePoints is type of Points that gives a constant score for a card placement
 */
public class SimplePoints implements Points {
    private final int points;

    /**
     * Constructs a new SimplePoints
     * @param points The constant number of point gained after the card placement
     */
    public SimplePoints(int points) {
        this.points = points;
    }

    /**
     * @param cp A reference to the card placement
     * @return Returns the constant points value associated to the card
     */
    @Override
    public int calculateScoredPoints(PlayArea.CardPlacement cp) {
        return points;
    }
}