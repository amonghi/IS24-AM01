package it.polimi.ingsw.am01.model.card.face;

import it.polimi.ingsw.am01.model.card.corner.Corner;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.util.Map;

public class BackCardFace extends BaseCardFace {
    private Map<Resource, Integer> resources;

    public BackCardFace(Corner tl, Corner tr, Corner br, Corner bl, Iterable<Resource> resources) {
        super(tl, tr, br, bl);
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Map<Resource, Integer> getCenterResources() {
        throw new UnsupportedOperationException("TODO");
    }
}
