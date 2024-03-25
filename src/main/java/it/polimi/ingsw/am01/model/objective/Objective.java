package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.model.collectible.Collectible;


/**
 * The common structure of an Objective card.
 * Objectives can give you points if you have a specific pattern or a
 * certain number of {@link Collectible}
 *
 * @see it.polimi.ingsw.am01.model.objective.PatternObjective
 * @see it.polimi.ingsw.am01.model.objective.SameCollectibleObjective
 * @see it.polimi.ingsw.am01.model.objective.DifferentCollectibleObjective
 *
 */

public abstract class Objective {
    private final int points;

    /**
     * Constructs a new Objective
     * @param points The number of points a player earns for each match
     */
    public Objective(int points) {
        this.points = points;
    }

    /**
     *
     * @return The points earned for each match
     */
    public int getPointsPerMatch() {
        return points;
    }

    /**
     *
     * @param pa The {@link PlayArea} on which find matches
     * @return the number of points earned by the player, considering all the matches
     */
    public abstract int getEarnedPoints(PlayArea pa);
}

