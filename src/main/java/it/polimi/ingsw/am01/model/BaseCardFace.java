package it.polimi.ingsw.am01.model;

import java.util.Map;
import java.util.Optional;

public abstract class BaseCardFace implements CardFace {
    private Corner tl;
    private Corner tr;
    private Corner br;
    private Corner bl;

    public BaseCardFace(Corner tl, Corner tr, Corner br, Corner bl) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Corner corner(CornerPosition cornerPosition) {
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

    @Override
    public Map<Resource, Integer> getCenterResources() {
        throw new UnsupportedOperationException("TODO");
    }
}
