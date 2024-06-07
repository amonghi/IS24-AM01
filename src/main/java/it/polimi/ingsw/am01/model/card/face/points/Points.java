package it.polimi.ingsw.am01.model.card.face.points;

import it.polimi.ingsw.am01.model.game.PlayArea;

/**
 * Points represents the score given to the player after placing a card
 */
public sealed interface Points permits CornerCoverPoints, ItemPoints, SimplePoints {
    /**
     * Calculates the score gained after the card placement depending on the type of Point
     * @param cp A reference to the card placement
     * @return Returns the calculated score
     */
    int calculateScoredPoints(PlayArea.CardPlacement cp);
}
