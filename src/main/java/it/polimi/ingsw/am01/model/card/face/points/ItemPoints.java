package it.polimi.ingsw.am01.model.card.face.points;

import it.polimi.ingsw.am01.model.card.CardPlacement;
import it.polimi.ingsw.am01.model.collectible.Item;

public class ItemPoints implements Points {
    private Item item;
    private int pointsPerItem;

    public ItemPoints(Item item, int pointsPerItem) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int calculateScoredPoints(CardPlacement cp) {
        throw new UnsupportedOperationException("TODO");
    }
}
