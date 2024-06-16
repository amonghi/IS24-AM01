package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.game.PlayArea;


/**
 * The common structure of an Objective card.
 * Objectives can give you points if you have a specific pattern or a
 * certain number of {@link Collectible}
 *
 * @see it.polimi.ingsw.am01.model.objective.PatternObjective
 * @see it.polimi.ingsw.am01.model.objective.SameCollectibleObjective
 * @see it.polimi.ingsw.am01.model.objective.DifferentCollectibleObjective
 */
public abstract sealed class Objective permits DifferentCollectibleObjective, PatternObjective, SameCollectibleObjective {
    private final int id;
    private final int points;

    /**
     * Constructs a new Objective
     *
     * @param id     The id of this objective
     * @param points The number of points a player earns for each match
     */
    public Objective(int id, int points) {
        this.points = points;
        this.id = id;
    }

    /**
     * @return The id of this objective
     */
    public int getId() {
        return id;
    }

    /**
     * @return The points earned for each match
     */
    public int getPointsPerMatch() {
        return points;
    }

    /**
     * @param pa The {@link PlayArea} on which find matches
     * @return the number of points earned by the player, considering all the matches
     */
    public abstract int getEarnedPoints(PlayArea pa);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Objective objective = (Objective) o;
        return id == objective.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + points;
        return result;
    }
}
