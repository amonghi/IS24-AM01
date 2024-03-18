package it.polimi.ingsw.am01.model.card.face;

import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.card.face.points.CornerCoverPoints;
import it.polimi.ingsw.am01.model.card.face.points.Points;
import it.polimi.ingsw.am01.model.card.face.points.SimplePoints;
import it.polimi.ingsw.am01.model.collectible.Item;
import it.polimi.ingsw.am01.model.collectible.Resource;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FrontCardFaceTest {
    @Test
    void createFrontCardFaceComplete() {
        SimplePoints simplePoints = new SimplePoints(2);
        Map<Resource, Integer> requiredResources = new EnumMap<>(Map.of(Resource.PLANT, 2));
        PlacementConstraint placementConstraint = new PlacementConstraint(requiredResources);
        Corner tr = Corner.filled(Resource.INSECT);
        Corner tl = Corner.missing();
        Corner br = Corner.filled(Resource.INSECT);
        Corner bl = Corner.empty();
        BaseCardFace front = new FrontCardFace(tr, tl, br, bl, placementConstraint, simplePoints);

        assertEquals(front.corner(CornerPosition.TOP_RIGHT), tr);
        assertEquals(front.corner(CornerPosition.TOP_LEFT), tl);
        assertEquals(front.corner(CornerPosition.BOTTOM_RIGHT), br);
        assertEquals(front.corner(CornerPosition.BOTTOM_LEFT), bl);
        assertEquals(front.getCenterResources(), Collections.emptyMap());
        assertEquals(front.getPoints(), Optional.of(simplePoints));
        assertEquals(front.getPlacementConstraint(), Optional.of(placementConstraint));
    }

    @Test
    void createFrontCardFaceNoConstraint() {
        CornerCoverPoints cornerCoverPoints = new CornerCoverPoints(2);
        Corner tr = Corner.filled(Item.MANUSCRIPT);
        Corner tl = Corner.empty();
        Corner br = Corner.empty();
        Corner bl = Corner.empty();
        BaseCardFace front = new FrontCardFace(tr, tl, br, bl, cornerCoverPoints);

        assertEquals(front.corner(CornerPosition.TOP_RIGHT), tr);
        assertEquals(front.corner(CornerPosition.TOP_LEFT), tl);
        assertEquals(front.corner(CornerPosition.BOTTOM_RIGHT), br);
        assertEquals(front.corner(CornerPosition.BOTTOM_LEFT), bl);
        assertEquals(front.getCenterResources(), Collections.emptyMap());
        assertEquals(front.getPoints(), Optional.of(cornerCoverPoints));
        assertEquals(front.getPlacementConstraint(), Optional.empty());
    }

    @Test
    void createFrontCardFaceNoConstraintNoPoint() {
        Corner tr = Corner.empty();
        Corner tl = Corner.empty();
        Corner br = Corner.empty();
        Corner bl = Corner.empty();
        BaseCardFace front = new FrontCardFace(tr, tl, br, bl);

        assertEquals(front.corner(CornerPosition.TOP_RIGHT), tr);
        assertEquals(front.corner(CornerPosition.TOP_LEFT), tl);
        assertEquals(front.corner(CornerPosition.BOTTOM_RIGHT), br);
        assertEquals(front.corner(CornerPosition.BOTTOM_LEFT), bl);
        assertEquals(front.getCenterResources(), Collections.emptyMap());
        assertEquals(front.getPoints(), Optional.empty());
        assertEquals(front.getPlacementConstraint(), Optional.empty());
    }
}