package it.polimi.ingsw.am01.model.card.face;

import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.card.face.points.Points;

import java.util.Optional;

public class FrontCardFace extends BaseCardFace {
    private PlacementConstraint placementConstraint;
    private Optional<Points> points;

    public FrontCardFace(Corner tl, Corner tr, Corner br, Corner bl, PlacementConstraint placementConstraint, Optional<Points> points) {
        super(tl, tr, br, bl);
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Optional<Points> getPoints() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Optional<PlacementConstraint> getPlacementConstraint() {
        throw new UnsupportedOperationException("TODO");
    }
}