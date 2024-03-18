package it.polimi.ingsw.am01.model.card.face.points;

import it.polimi.ingsw.am01.model.game.PlayArea;

/**
 * CornerCoverPoints is a type of Points that gives an amount of points per every corner covered by placing the card
 */
public class CornerCoverPoints implements Points {
    private final int pointsPerCorner;

    /**
     * Constructs a new CornerCoverPoints
     * @param pointsPerCorner The amount of points gained per corner covered
     */
    public CornerCoverPoints(int pointsPerCorner) {
        this.pointsPerCorner = pointsPerCorner;
    }

    /**
     * Counts how many corners the card is covering after placing it and calculate the score
     * @param cp A reference to the card placement
     * @return Returns the calculated score
     */
    @Override
    public int calculateScoredPoints(PlayArea.CardPlacement cp) {
        int countedCorners = cp.getCovered().keySet().size();
        return countedCorners * pointsPerCorner;
    }
}
