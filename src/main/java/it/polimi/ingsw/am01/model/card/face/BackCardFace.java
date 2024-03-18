package it.polimi.ingsw.am01.model.card.face;

import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

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
     * @param tl the top-left corner
     * @param tr the top-right corner
     * @param bl the bottom-left corner
     * @param br the bottom-right corner
     * @param resources the map with the permanent resources of this face
     */

    public BackCardFace(Corner tr, Corner tl, Corner br, Corner bl, Map<Resource, Integer> resources) {
        super(tr, tl, br, bl);
        this.resources = new HashMap<>(resources);
    }

    /**
     *
     * @return the map with the permanent resources of this face
     */
    @Override
    public Map<Resource, Integer> getCenterResources() {
        return resources;
    }
}
