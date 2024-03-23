package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.collectible.Resource;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

class PlayAreaTest {

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
    void placesInitialCard() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);

        Optional<PlayArea.CardPlacement> optInitialPlacement = playArea.getAt(PlayArea.Position.ORIGIN);
        assertTrue(optInitialPlacement.isPresent());

        PlayArea.CardPlacement cp0 = optInitialPlacement.get();
        assertEquals(playArea, cp0.getPlayArea());
        assertEquals(PlayArea.Position.ORIGIN, cp0.getPosition());
        assertEquals(starterCard, cp0.getCard());
        assertEquals(Side.FRONT, cp0.getSide());
        assertEquals(starterCardFF, cp0.getVisibleFace());
        assertEquals(0, cp0.getPoints());
    }

    @Test
    void canPlace() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        PlayArea.CardPlacement cp1 = playArea.placeAt(1, 0, aCard, Side.BACK);
        assertEquals(playArea, cp1.getPlayArea());
        assertEquals(new PlayArea.Position(1, 0), cp1.getPosition());
        assertEquals(aCard, cp1.getCard());
        assertEquals(Side.BACK, cp1.getSide());
        assertEquals(aCardBF, cp1.getVisibleFace());
        assertEquals(0, cp1.getPoints());
    }

    @Test
    void cantPlaceWithoutConnecting() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        assertThrows(IllegalPlacementException.class, () -> playArea.placeAt(100, 100, aCard, Side.FRONT));
    }

    @Test
    void cantPlaceOverFilledCorner() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        playArea.placeAt(1, 0, aCard, Side.FRONT);
        assertThrows(IllegalPlacementException.class, () -> playArea.placeAt(1, -1, aCard, Side.FRONT));
    }

    @Test
    void getAtIjSameAsGetAtPosition() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        PlayArea.CardPlacement cp = playArea.placeAt(1, 0, aCard, Side.FRONT);

        Optional<PlayArea.CardPlacement> cp1 = playArea.getAt(1, 0);
        assertTrue(cp1.isPresent());
        assertSame(cp, cp1.get());

        Optional<PlayArea.CardPlacement> cp2 = playArea.getAt(new PlayArea.Position(1, 0));
        assertTrue(cp2.isPresent());
        assertSame(cp, cp2.get());
    }

    @Test
    void canIterateOverCardPlacements() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        //noinspection OptionalGetWithoutIsPresent
        PlayArea.CardPlacement cp0 = playArea.getAt(PlayArea.Position.ORIGIN).get();

        Iterator<PlayArea.CardPlacement> iterator = playArea.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(cp0, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void countsResources() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);

        assertEquals(0, playArea.getScore());
        assertEquals(
                Map.of(
                        Resource.PLANT, 1,
                        Resource.FUNGI, 1,
                        Resource.ANIMAL, 1,
                        Resource.INSECT, 1
                ),
                playArea.getCollectibleCount()
        );

        playArea.placeAt(1, 0, aCard, Side.BACK);

        assertEquals(0, playArea.getScore());
        assertEquals(
                Map.of(
                        Resource.PLANT, 0,
                        Resource.FUNGI, 2,
                        Resource.ANIMAL, 1,
                        Resource.INSECT, 1
                ),
                playArea.getCollectibleCount()
        );
    }

    @Test
    void placementsHaveRelatives() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        //noinspection OptionalGetWithoutIsPresent
        PlayArea.CardPlacement cp0 = playArea.getAt(PlayArea.Position.ORIGIN).get();

        assertTrue(cp0.getRelative(CornerPosition.TOP_RIGHT).isEmpty());

        PlayArea.CardPlacement cp1 = playArea.placeAt(1, 0, aCard, Side.FRONT);

        assertEquals(Optional.of(cp1), cp0.getRelative(CornerPosition.TOP_RIGHT));
        assertEquals(Optional.of(cp0), cp1.getRelative(CornerPosition.BOTTOM_LEFT));
    }

    @Test
    void newPlacementCoversOld() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        //noinspection OptionalGetWithoutIsPresent
        PlayArea.CardPlacement cp0 = playArea.getAt(PlayArea.Position.ORIGIN).get();

        assertEquals(cp0, cp0.getTopPlacementAtCorner(CornerPosition.TOP_RIGHT));
        assertEquals(Optional.of(Resource.PLANT), cp0.getVisibleCollectibleAtCorner(CornerPosition.TOP_RIGHT));
        assertEquals(Map.of(), cp0.getCovered());

        PlayArea.CardPlacement cp1 = playArea.placeAt(1, 0, aCard, Side.FRONT);

        assertTrue(cp0.compareTo(cp1) < 0);
        assertTrue(cp1.compareTo(cp0) > 0);

        assertEquals(cp1, cp0.getTopPlacementAtCorner(CornerPosition.TOP_RIGHT));
        assertEquals(Optional.of(Resource.FUNGI), cp0.getVisibleCollectibleAtCorner(CornerPosition.TOP_RIGHT));
        assertEquals(Map.of(), cp0.getCovered());

        assertEquals(Map.of(CornerPosition.BOTTOM_LEFT, cp0), cp1.getCovered());
    }

}
