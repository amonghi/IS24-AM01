package it.polimi.ingsw.am01.model.card.face;

import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.card.face.points.Points;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Represent the face of Card on which are placed all the components,
 * (i.e. {@code Corner}, {@code PlacementConstraint}, {@code Points})
 *
 * @see it.polimi.ingsw.am01.model.card.face.CardFace
 * @see it.polimi.ingsw.am01.model.card.face.FrontCardFace
 * @see it.polimi.ingsw.am01.model.card.face.BackCardFace
 *
 */

public abstract class BaseCardFace implements CardFace {
    private final Corner tl;
    private final Corner tr;
    private final Corner bl;
    private final Corner br;

    /**
     * Constructs the {@code BaseCardFace} defining the four {@code Corner}s
     *
     * @param tl the top-left corner
     * @param tr the top-right corner
     * @param bl the bottom-left corner
     * @param br the bottom-right corner
     *
     */
    public BaseCardFace(Corner tr, Corner tl, Corner br, Corner bl) {
        this.tl = tl;
        this.tr = tr;
        this.bl = bl;
        this.br = br;
    }

    /**
     * Default implementation of this method.
     *
     * @param cornerPosition the {@code CornerPosition} of the corner to be returned
     * @return the {@code Corner} at a specific {@code CornerPosition}
     */
    @Override
    public Corner corner(CornerPosition cornerPosition) {
        return switch (cornerPosition) {
            case TOP_LEFT -> tl;
            case TOP_RIGHT -> tr;
            case BOTTOM_LEFT -> bl;
            case BOTTOM_RIGHT -> br;
        };
    }

    /**
     * Default implementation of this method.
     * @see it.polimi.ingsw.am01.model.card.face.FrontCardFace
     *
     * @return an empty {@code Optional<Points>}
     */
    @Override
    public Optional<Points> getPoints() {
        return Optional.empty();
    }

    /**
     * Default implementation of this method
     * @see it.polimi.ingsw.am01.model.card.face.FrontCardFace
     *
     * @return an empty {@code Optional<PlacementConstraint>}
     */
    @Override
    public Optional<PlacementConstraint> getPlacementConstraint() {
        return Optional.empty();
    }

    /**
     * Default implementation of this method
     * @see it.polimi.ingsw.am01.model.card.face.BackCardFace
     *
     * @return an empty {@code Map<Resource, Integer>}
     */
    @Override
    public Map<Resource, Integer> getCenterResources() {
        return Collections.emptyMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseCardFace that = (BaseCardFace) o;
        return Objects.equals(tl, that.tl) && Objects.equals(tr, that.tr) && Objects.equals(bl, that.bl) && Objects.equals(br, that.br);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(tl, tr, bl, br);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "BaseCardFace{" +
                "tl=" + tl +
                ", tr=" + tr +
                ", bl=" + bl +
                ", br=" + br +
                '}';
    }
}
