package it.polimi.ingsw.am01.model.card.face.points;

import it.polimi.ingsw.am01.model.game.PlayArea;

/**
 * SimplePoints is type of Points that gives a constant score for a card placement
 */
public record SimplePoints(int points) implements Points {
    /**
     * @param cp A reference to the card placement
     * @return Returns the constant points value associated to the card
     */
    @Override
    public int calculateScoredPoints(PlayArea.CardPlacement cp) {
        return points;
    }
}
