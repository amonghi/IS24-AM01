package it.polimi.ingsw.am01.model.card.face;

import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The back face of a card.
 * Extends {@code BaseCardFace} and overrides all the methods involving the components of the back face,
 * i.e. the center resources
 *
 * @see it.polimi.ingsw.am01.model.card.face.BaseCardFace
 */

public class BackCardFace extends BaseCardFace {
    private final Map<Resource, Integer> resources;

    /**
     * Constructs a new {@code BackCardFace}
     *
     * @param tl        the top-left corner
     * @param tr        the top-right corner
     * @param bl        the bottom-left corner
     * @param br        the bottom-right corner
     * @param resources the map with the permanent resources of this face
     */

    public BackCardFace(Corner tr, Corner tl, Corner br, Corner bl, Map<Resource, Integer> resources) {
        super(tr, tl, br, bl);
        this.resources = new HashMap<>(resources);
    }

    /**
     * @return an unmodifiable map with the permanent resources of this face
     */
    @Override
    public Map<Resource, Integer> getCenterResources() {
        return Collections.unmodifiableMap(resources);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BackCardFace that = (BackCardFace) o;
        return Objects.equals(resources, that.resources);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), resources);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "BackCardFace{" +
                "tl=" + corner(CornerPosition.TOP_LEFT) +
                ", tr=" + corner(CornerPosition.TOP_RIGHT) +
                ", bl=" + corner(CornerPosition.BOTTOM_LEFT) +
                ", br=" + corner(CornerPosition.BOTTOM_RIGHT) +
                ", resources=" + resources +
                '}';
    }
}
