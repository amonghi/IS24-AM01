package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.game.PlayArea;


/**
 * An objective whose requirements are to have a minimum number
 * of a specific {@link Collectible}
 *
 */

public class SameCollectibleObjective extends Objective {
    private final Collectible requiredCollectible;
    private final int requiredNumber;
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
     * @return the {@link Collectible} required to earn the points
     */
    public Collectible getRequiredCollectible() {
        return requiredCollectible;
    }

    /**
     * @return the number of {@link Collectible} required to earn points
     */
    public int getRequiredNumber() {
        return requiredNumber;
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
