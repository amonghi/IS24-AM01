package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.game.PlayArea;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *  An objective that gives point if you have a certain set of resources.
 *
 */

public class CollectibleObjective extends Objective {
    private final Map<Collectible, Integer> requiredCollectibles;

    public CollectibleObjective(int points, Map<Collectible, Integer> requiredCollectibles) {
        super(points);
        this.requiredCollectibles = new HashMap<>(requiredCollectibles);
    }

    @Override
    public int getEarnedPoints(PlayArea pa) {
        return 0;
        //TODO
    }
}
