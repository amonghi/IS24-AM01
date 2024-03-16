package it.polimi.ingsw.am01.model.panel.placement;

import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.game.PlayArea;

import java.util.Map;

public class PlacementConstraint {
    private Map<Resource, Integer> requiredResources;

    public PlacementConstraint(Map<Resource, Integer> requiredResources) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isSatisfied(PlayArea playArea) {
        throw new UnsupportedOperationException("TODO");
    }
}
