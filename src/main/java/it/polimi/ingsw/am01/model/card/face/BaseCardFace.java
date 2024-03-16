package it.polimi.ingsw.am01.model.card.face;

import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.card.face.points.Points;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;

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
    public Optional<Points> getPoints() {
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
