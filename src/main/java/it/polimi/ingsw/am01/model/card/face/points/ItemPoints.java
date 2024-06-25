package it.polimi.ingsw.am01.model.card.face.points;

import it.polimi.ingsw.am01.model.collectible.Item;
import it.polimi.ingsw.am01.model.game.PlayArea;

/**
 * ItemPoints is type of Points that gives a score for a card placement based on the amount of a specific item
 */
public record ItemPoints(Item item, int pointsPerItem) implements Points {
    /**
     * Counts the amount of the specified item and calculate the score gained after placing the card
     *
     * @param cp A reference to the card placement
     * @return Returns the calculated score
     */
    @Override
    public int calculateScoredPoints(PlayArea.CardPlacement cp) {
        int countedItems = cp.getPlayArea().getCollectibleCount().getOrDefault(item, 0);
        return countedItems * pointsPerItem;
    }
}

