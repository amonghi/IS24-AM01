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

    //FIX: Iterable<Resource> should be changed in Map<Resource, Integer>?
    //This is the implementation with Iterable<Resource>
    /*
    public BackCardFace(Corner tl, Corner tr, Corner br, Corner bl, Iterable<Resource> resources) {
        super(tl, tr, br, bl);
        this.resources = new HashMap<>();
        fillResources(resources);
    }

    private void fillResources(Iterable<Resource> resources) {
        for (Resource res : resources) {
            if(!this.resources.containsKey(res)) {
                this.resources.put(res, 0);
            }
            else {
                this.resources.replace(res, this.resources.get(res), this.resources.get(res)+1);
            }
        }
    }
    */

    public BackCardFace(Corner tl, Corner tr, Corner br, Corner bl, Map<Resource, Integer> resources) {
        super(tl, tr, br, bl);
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
