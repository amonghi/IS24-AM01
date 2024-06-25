package it.polimi.ingsw.am01.model.card.face.points;

import it.polimi.ingsw.am01.model.game.PlayArea;

/**
 * CornerCoverPoints is a type of Points that gives an amount of points per every corner covered by placing the card
 */
public record CornerCoverPoints(int pointsPerCorner) implements Points {
    /**
     * Counts how many corners the card is covering after placing it and calculate the score
     *
     * @param cp A reference to the card placement
     * @return Returns the calculated score
     */
    @Override
    public int calculateScoredPoints(PlayArea.CardPlacement cp) {
        int countedCorners = cp.getCovered().keySet().size();
        return countedCorners * pointsPerCorner;
    }
}

