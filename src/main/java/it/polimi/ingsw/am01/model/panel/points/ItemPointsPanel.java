package it.polimi.ingsw.am01.model.panel.points;

import it.polimi.ingsw.am01.model.card.CardPlacement;
import it.polimi.ingsw.am01.model.collectible.Item;

public class ItemPointsPanel implements PointsPanel {
    private Item item;
    private int pointsPerItem;

    public ItemPointsPanel(Item item, int pointsPerItem) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int calculateScoredPoints(CardPlacement cp) {
        throw new UnsupportedOperationException("TODO");
    }
}
