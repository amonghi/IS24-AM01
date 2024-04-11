package it.polimi.ingsw.am01.model.card.face;

import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.card.face.points.Points;
import it.polimi.ingsw.am01.model.collectible.Resource;

import java.util.Map;
import java.util.Optional;

/**
 * This interface defines all the methods for {@code BaseCardFace}.
 * The default implementation of each method is defined in {@code BaseCardFace}
 * @see it.polimi.ingsw.am01.model.card.face.BaseCardFace
 */
public interface CardFace {

    /**
     * This method provides the {@link Corner} located in a specific {@link CornerPosition}
     * @see it.polimi.ingsw.am01.model.card.face.corner.Corner
     * @see it.polimi.ingsw.am01.model.card.face.corner.CornerPosition
     * @param cornerPosition the position of the required {@code Corner}
     * @return the {@code Corner} of card at {@code cornerPosition}
     */
    Corner corner(CornerPosition cornerPosition);

    /**
     * This method provides all data on how to get points through a {@code CardPlacement}
     * @see it.polimi.ingsw.am01.model.card.face.FrontCardFace
     * @see it.polimi.ingsw.am01.model.card.face.points.Points
     * @see it.polimi.ingsw.am01.model.game.PlayArea.CardPlacement
     * @return a {@code Points} if the card has a "score" associated with it, otherwise an {@code Optional.empty()}
     */
    Optional<Points> getPoints();

    /**
     * This method provides the requirements for a card placement
     * @see it.polimi.ingsw.am01.model.card.face.FrontCardFace
     * @see it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint
     * @return a {@code PlacementConstraint} if the card has placement requirements, otherwise an {@code Optional.empty()}
     */
    Optional<PlacementConstraint> getPlacementConstraint();

    /**
     * This method provides all center {@link Resource} of a card face
     * @see it.polimi.ingsw.am01.model.card.face.BackCardFace
     * @see it.polimi.ingsw.am01.model.collectible.Resource
     * @return a map that contains the amount of each {@code Resource}
     */
    Map<Resource, Integer> getCenterResources();
}