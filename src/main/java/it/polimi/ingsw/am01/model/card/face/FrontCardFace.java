package it.polimi.ingsw.am01.model.card.face;

import it.polimi.ingsw.am01.model.card.corner.Corner;
import it.polimi.ingsw.am01.model.panel.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.panel.points.PointsPanel;

import java.util.Optional;

public class FrontCardFace extends BaseCardFace {
    private PlacementConstraint placementConstraint;
    private Optional<PointsPanel> pointsPanel;

    public FrontCardFace(Corner tl, Corner tr, Corner br, Corner bl, PlacementConstraint placementConstraint, Optional<PointsPanel> pointsPanel) {
        super(tl, tr, br, bl);
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Optional<PointsPanel> getPointsPanel() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Optional<PlacementConstraint> getPlacementConstraint() {
        throw new UnsupportedOperationException("TODO");
    }
}