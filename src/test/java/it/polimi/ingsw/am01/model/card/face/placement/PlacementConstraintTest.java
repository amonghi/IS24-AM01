package it.polimi.ingsw.am01.model.card.face.placement;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.points.SimplePoints;
import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.game.PlayArea;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlacementConstraintTest {

    // card 81
    final FrontCardFace starterCardFF = new FrontCardFace(
            Corner.filled(Resource.PLANT),
            Corner.filled(Resource.FUNGI),
            Corner.filled(Resource.ANIMAL),
            Corner.filled(Resource.INSECT)
    );
    final BackCardFace starterCardBF = new BackCardFace(
            Corner.filled(Resource.PLANT),
            Corner.empty(),
            Corner.empty(),
            Corner.filled(Resource.INSECT),
            Map.of(Resource.INSECT, 1)
    );
    final Card starterCard = new Card(
            CardColor.NEUTRAL,
            true,
            false,
            starterCardFF,
            starterCardBF
    );

    // card 1
    final FrontCardFace aCardFF = new FrontCardFace(
            Corner.empty(),
            Corner.filled(Resource.FUNGI),
            Corner.missing(),
            Corner.filled(Resource.FUNGI)
    );
    final BackCardFace aCardBF = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Map.of(Resource.FUNGI, 1)
    );
    final Card aCard = new Card(
            CardColor.RED,
            true,
            false,
            aCardFF,
            aCardBF
    );

    @Test
    void canVerifyIfSatisfied() {
        final PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        playArea.placeAt(1, 0, aCard, Side.BACK);
        Map<Resource, Integer> constraintResources = new HashMap<>();
        constraintResources.put(Resource.ANIMAL, 1);
        constraintResources.put(Resource.FUNGI, 1);
        constraintResources.put(Resource.INSECT, 1);
        PlacementConstraint placementConstraint = new PlacementConstraint(constraintResources);
        assertTrue(placementConstraint.isSatisfied(playArea));
    }

    @Test
    void canVerifyIfNotSatisfied() {
        final PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        playArea.placeAt(1, 0, aCard, Side.BACK);
        Map<Resource, Integer> constraintResources = new HashMap<>();
        constraintResources.put(Resource.ANIMAL, 3);
        PlacementConstraint placementConstraint = new PlacementConstraint(constraintResources);
        assertFalse(placementConstraint.isSatisfied(playArea));
    }

    @Test
    void throwsIfEmptyConstraint() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        playArea.placeAt(1, 0, aCard, Side.BACK);
        Map<Resource, Integer> constraintResources = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> {
            new PlacementConstraint(constraintResources);
        });
    }
}