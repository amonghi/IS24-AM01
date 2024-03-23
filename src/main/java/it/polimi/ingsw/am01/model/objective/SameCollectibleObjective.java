package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.game.PlayArea;

import java.util.Map;

/**
 * An objective whose requirements are to have a minimum number
 * of a specific {@link Collectible}
 *
 */

public class SameCollectibleObjective extends Objective {
    Collectible requiredCollectible;
    int requiredNumber;
    /**
     * Constructs a new SameCollectibleObjective
     *
     * @param points the points a player earns for each match
     * @param requiredCollectible the {@link Collectible} required to earn the points
     * @param requiredNumber the number of {@link Collectible} required to earn points
     *
     */
    public SameCollectibleObjective(int points, Collectible requiredCollectible, int requiredNumber) {
        super(points);
        this.requiredCollectible = requiredCollectible;
        this.requiredNumber = requiredNumber;
    }

    /**
     * Calculates the points earned by the player who meets this objective
     * checking whether there is the required number, or a multiple of it,
     * of the required {@link Collectible} on the {@link PlayArea}
     *
     * @param pa The {@link PlayArea} where the requirements have to be verified
     * @return the sum of the points earned by the player
     *
     */
    @Override
    public int getEarnedPoints(PlayArea pa) {
        return pa.getCollectibleCount().containsKey(requiredCollectible) ?
                (pa.getCollectibleCount().get(requiredCollectible) / requiredNumber) * getPointsPerMatch() : 0;
    }
}
