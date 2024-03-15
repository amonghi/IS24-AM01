package it.polimi.ingsw.am01.model;

import java.util.Map;
import java.util.Optional;

public interface CardFace {
    Corner corner(CornerPosition cornerPosition);

    Optional<PointsPanel> getPointsPanel();

    Optional<PlacementConstraint> getPlacementConstraint();

    Map<Resource, Integer> getCenterResources();
}
