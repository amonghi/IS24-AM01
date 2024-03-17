package it.polimi.ingsw.am01.model.card.face;

import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.card.face.points.Points;

import java.util.Map;
import java.util.Optional;

/**
 * This interface defines all the methods for {@code BaseCardFace}.
 * The default implementation of each method is defined in {@code BaseCardFace}
 *
 * @see it.polimi.ingsw.am01.model.card.face.BaseCardFace
 */

public interface CardFace {
    Corner corner(CornerPosition cornerPosition);

    Optional<Points> getPoints();

    Optional<PlacementConstraint> getPlacementConstraint();

    Map<Resource, Integer> getCenterResources();
}
