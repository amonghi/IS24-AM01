package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.model.card.CardPlacement;

import java.util.Map;
import java.util.Set;

public class CollectibleObjective extends Objective {
    private Map<Collectible, Integer> requiredCollectibles;

    public CollectibleObjective(int points, Map<Collectible, Integer> requiredCollectibles) {
        super(points);
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Set<Set<CardPlacement>> test(PlayArea pa) {
        throw new UnsupportedOperationException("TODO");
    }
}