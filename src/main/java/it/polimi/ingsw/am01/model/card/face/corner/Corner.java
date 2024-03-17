package it.polimi.ingsw.am01.model.card.face.corner;

import it.polimi.ingsw.am01.model.collectible.Collectible;

import java.util.Optional;

/**
 * A corner in a {@code BaseCardFace}.
 * It may be either missing or not.
 * If it's not missing, it can either contain a {@code Collectible} or be empty.
 *
 * @see it.polimi.ingsw.am01.model.card.face.BaseCardFace
 */

public class Corner {
    private final boolean socket;

    //FIX: Decide how to manage Optionals
    //private Optional<Collectible> collectible;
    private final Collectible collectible;

    /**
     * Constructs a new corner to be returned by one of the three methods:
     * {@code Corner.missing()}, {@code Corner.empty()}, {@code Corner.filled(Collectible)}
     */
    private Corner(Collectible collectible, boolean socket) {
        this.collectible = collectible;
        this.socket = socket;
    }

    /**
     *
     * @return a new missing corner, i.e. a corner for which {@code Corner.socket} is false
     */
    public static Corner missing() {
        return new Corner(null, false);
    }

    /**
     *
     * @return a new empty corner, i.e. a corner with no {@code Collectible}
     */
    public static Corner empty() {
        return new Corner(null, true);
    }

    /**
     *
     * @param collectible the {@code Collectible} to place in the corner
     * @return a new corner with a {@code Collectible} in it
     */
    public static Corner filled(Collectible collectible) {
        return new Corner(collectible, true);
    }

    /**
     *
     * @return whether a corner can be used as a socket or not, i.e. whether it's a missing corner or not
     */
    public boolean isSocket() {
        return socket;
    }

    /**
     *
     * @return the collectible, if any, contained in the corner
     */
    public Optional<Collectible> getCollectible() {
        return collectible == null ? Optional.empty() : Optional.of(collectible);
    }
}
