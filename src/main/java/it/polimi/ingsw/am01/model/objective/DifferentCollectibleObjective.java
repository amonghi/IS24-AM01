package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.collectible.Item;
import it.polimi.ingsw.am01.model.game.PlayArea;

import java.util.HashSet;
import java.util.Set;

/**
 * An objective whose requirements are to have a minimum number
 * of a certain set of {@link Item}s
 *
 */
public class DifferentCollectibleObjective extends Objective {
    private final Set<Item> requiredItems;
    /**
     * Constructs a new DifferentCollectibleObjective
     *
     * @param points the points a player earns for each match
     * @param requiredItems the set of items required to earn the points
     *
     */
    public DifferentCollectibleObjective(int points, Set<Item> requiredItems) {
        super(points);
        this.requiredItems = new HashSet<>(requiredItems);
    }

    /**
     * Calculates the points earned by the player who meets this objective
     * checking whether there is the required number, or a multiple of it,
     * of the required set of {@link Item} on the {@link PlayArea}
     *
     * @param pa The {@link PlayArea} where the requirements have to be verified
     * @return the sum of the points earned by the player
     *
     */
    @Override
    public int getEarnedPoints(PlayArea pa) {
        return pa.getCollectibleCount().keySet().containsAll(requiredItems) ?
                getPointsPerMatch() * requiredItems.stream()
                                        .mapToInt(item -> pa.getCollectibleCount().get(item))
                                        .min()
                                        .orElse(0)
                : 0;
    }
}
