package it.polimi.ingsw.am01.model.card.face.points;

import it.polimi.ingsw.am01.model.collectible.Item;
import it.polimi.ingsw.am01.model.game.PlayArea;

import java.util.Objects;

/**
 * ItemPoints is type of Points that gives a score for a card placement based on the amount of a specific item
 */
public final class ItemPoints implements Points {
    private final Item item;
    private final int pointsPerItem;

    /**
     * Constructs a new ItemPoints
     * @param item The type of item that gives point
     * @param pointsPerItem The amount of points gained per item of the specified type
     */
    public ItemPoints(Item item, int pointsPerItem) {
        this.item = item;
        this.pointsPerItem = pointsPerItem;
    }

    /**
     * @return the type of item that gives point
     */
    public Item getItem() {
        return item;
    }

    /**
     * @return the amount of points gained per item of the specified type
     */
    public int getPointsPerItem() {
        return pointsPerItem;
    }

    /**
     * Counts the amount of the specified item and calculate the score gained after placing the card
     * @param cp A reference to the card placement
     * @return Returns the calculated score
     */
    @Override
    public int calculateScoredPoints(PlayArea.CardPlacement cp) {
        int countedItems = cp.getPlayArea().getCollectibleCount().getOrDefault(item, 0);
        return countedItems * pointsPerItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPoints that = (ItemPoints) o;
        return pointsPerItem == that.pointsPerItem && item == that.item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(item, pointsPerItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ItemPoints{" +
                "item=" + item +
                ", pointsPerItem=" + pointsPerItem +
                '}';
    }
}
