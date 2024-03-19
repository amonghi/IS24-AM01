package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.game.PlayArea;

import java.util.Set;

/**
 * The common structure of an Objective card.
 * Objectives can give you points if you have a specific pattern o a certain amount of collectible.
 *
 * @see it.polimi.ingsw.am01.model.objective.PatternObjective
 * @see it.polimi.ingsw.am01.model.objective.CollectibleObjective
 *
 */

public abstract class Objective {
    private final int points;

    /**
     * Constructs a new Objective
     * @param points The number of points for each match
     */
    public Objective(int points) {
        this.points = points;
    }

    /**
     *
     * @return The points for a specific match
     */
    public int getPointsPerMatch() {
        return points;
    }

    /**
     *
     * @param pa The {@link PlayArea} on which calculate matches
     * @return the amount of points earned
     */
    public abstract int getEarnedPoints(PlayArea pa);
}

