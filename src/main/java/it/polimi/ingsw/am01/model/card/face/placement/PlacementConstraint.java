package it.polimi.ingsw.am01.model.card.face.placement;

import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.game.PlayArea;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

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
     * @return an unmodifiable map that associate each resource with the necessary amount
     */
    public Map<Resource, Integer> getRequiredResources() {
        return Collections.unmodifiableMap(requiredResources);
    }

    /**
     * Check if the amount of resources in the PlayArea are enough to place the card with that PlacementConstraint
     * @param playArea A reference to the PlayArea to check
     * @return Returns whether the constraint is satisfied
     */
    public boolean isSatisfied(PlayArea playArea) {
        for (Resource res : requiredResources.keySet()) {
            if (requiredResources.get(res) > playArea.getCollectibleCount().getOrDefault(res, 0))
                return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlacementConstraint that = (PlacementConstraint) o;
        return Objects.equals(requiredResources, that.requiredResources);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(requiredResources);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "PlacementConstraint{" +
                "requiredResources=" + requiredResources +
                '}';
    }
}
