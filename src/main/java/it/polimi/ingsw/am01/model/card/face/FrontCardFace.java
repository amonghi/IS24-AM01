package it.polimi.ingsw.am01.model.card.face;

import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.card.face.points.Points;

import java.util.Optional;

/**
 * The front face of a card.
 * Extends {@code BaseCardFace} and overrides all the methods involving the components of the front face,
 * i.e. {@code PlacementConstraint} and {@code Points}
 *
 * @see it.polimi.ingsw.am01.model.card.face.BaseCardFace
 * @see it.polimi.ingsw.am01.model.card.face.points.Points
 * @see it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint
 */

public class FrontCardFace extends BaseCardFace {
    private final PlacementConstraint placementConstraint;

    //FIX: Decide how to manage Optionals
    //private Optional<Points> points;
    private final Points points;

    /**
     * Constructs a new {@code FrontCardFace} with a {@code PlacementConstraint}
     *
     * @param tl the top-left corner
     * @param tr the top-right corner
     * @param bl the bottom-left corner
     * @param br the bottom-right corner
     * @param placementConstraint an object containing a map with the number of resources, of each type, needed to place the card
     * @param points an object which define how the card allows you to earn points, if any
     *
     */

    //FIX: Decide how to manage Optionals as param of a method
    //public FrontCardFace(Corner tl, Corner tr, Corner br, Corner bl, PlacementConstraint placementConstraint, Optional<Points> points) {
    public FrontCardFace(Corner tl, Corner tr, Corner br, Corner bl, PlacementConstraint placementConstraint, Points points) {
        super(tl, tr, br, bl);
        this.placementConstraint = placementConstraint;
        this.points = points;
    }

    /**
     * Constructs a new {@code FrontCardFace} with no {@code PlacementConstraint}
     *
     * @param tl the top-left corner
     * @param tr the top-right corner
     * @param bl the bottom-left corner
     * @param br the bottom-right corner
     * @param points an object which define how the card allows you to earn points, if any
     *
     */
    public FrontCardFace(Corner tl, Corner tr, Corner br, Corner bl, Points points) {
        super(tl, tr, br, bl);
        this.points = points;
        this.placementConstraint = null;
    }

    /**
     * Override the default method defined in {@code BaseCardFace}
     * @see it.polimi.ingsw.am01.model.card.face.BaseCardFace
     *
     * @return an object which define how the card allows you to earn points, if any.
     * If the card doesn't earn any points, it returns {@code Optional.empty()}
     *
     */
    @Override
    public Optional<Points> getPoints() {
        return points == null ? Optional.empty() : Optional.of(points);
    }

    /**
     * Override the default method defined in {@code BaseCardFace}
     * @see it.polimi.ingsw.am01.model.card.face.BaseCardFace
     *
     * @return the {@code PlacementConstraint} for this card
     */
    @Override
    public Optional<PlacementConstraint> getPlacementConstraint() {
        return Optional.ofNullable(placementConstraint);
    }
}