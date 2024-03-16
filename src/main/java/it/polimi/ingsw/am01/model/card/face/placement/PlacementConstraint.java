package it.polimi.ingsw.am01.model.card.face.placement;

import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.game.PlayArea;

import java.util.EnumMap;
import java.util.Map;

/**
 * PlacementConstraint represents the amount of resources necessary to place a card inside a PlayArea
 */
public class PlacementConstraint {
    private final Map<Resource, Integer> requiredResources;

    /**
     * Constructs a new PlacementConstraint
     * @param requiredResources A map that associate each resource with the necessary amount
     */
    public PlacementConstraint(Map<Resource, Integer> requiredResources) {
        this.requiredResources = new EnumMap<>(requiredResources);
    }

    /**
     * Check if the amount of resources in the PlayArea are enough to place the card with that PlacementConstraint
     * @param playArea A reference to the PlayArea to check
     * @return Returns whether the constraint is satisfied
     */
    public boolean isSatisfied(PlayArea playArea) {
        for (Resource res : requiredResources.keySet()) {
            if (requiredResources.get(res) > playArea.getCollectibleCount().get(res))
                return false;
        }
        return true;
    }
}
