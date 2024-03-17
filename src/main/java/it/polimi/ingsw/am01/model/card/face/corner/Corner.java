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
     * Constructs a new corner, with a collectible in it, to be returned by {@code Corner.filled(Collectible)}
     *
     * @param socket a boolean value which specify if a corner can be used as a link or not
     * @param collectible either a {@code Resource} or an {@code Item} contained in the corner
     */
    private Corner(Collectible collectible, boolean socket) {
        this.collectible = collectible;
        this.socket = socket;
    }

    /**
     * Construct a new corner, either empty or missing, to be returned by one of the two methods:
     * {@code Corner.missing()}, {@code Corner.empty()}
     *
     * @param socket a boolean value which specify if a corner can be used as a link or not
     *
     */
    private Corner(boolean socket) {
        this.socket = socket;
        this.collectible = null;
    }

    /**
     *
     * @return a new missing corner, i.e. a corner for which {@code Corner.socket} is false
     */
    public static Corner missing() {
        return new Corner(false);
    }

    /**
     *
     * @return a new empty corner, i.e. a corner with no {@code Collectible}
     */
    public static Corner empty() {
        return new Corner(true);
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
        return Optional.ofNullable(collectible);
    }
}
