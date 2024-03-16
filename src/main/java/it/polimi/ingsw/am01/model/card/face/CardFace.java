package it.polimi.ingsw.am01.model.card.face;

import it.polimi.ingsw.am01.model.card.corner.Corner;
import it.polimi.ingsw.am01.model.card.corner.CornerPosition;
import it.polimi.ingsw.am01.model.panel.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.panel.points.PointsPanel;

import java.util.Map;
import java.util.Optional;

public interface CardFace {
    Corner corner(CornerPosition cornerPosition);

    Optional<PointsPanel> getPointsPanel();

    Optional<PlacementConstraint> getPlacementConstraint();

    Map<Resource, Integer> getCenterResources();
}
