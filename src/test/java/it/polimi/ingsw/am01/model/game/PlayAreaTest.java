package it.polimi.ingsw.am01.model.game;

import com.google.gson.internal.PreJava9DateFormatProvider;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.card.face.corner.CornerPosition;
import it.polimi.ingsw.am01.model.collectible.Resource;
import org.junit.jupiter.api.Test;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

class PlayAreaTest {

    GameAssets assets = GameAssets.getInstance();
    final Card starterCard = assets.getStarterCards().get(0);
    final Card aCard = assets.getResourceCards().get(0);
    final Card bCard = assets.getResourceCards().get(1);
    final Card cCard = assets.getResourceCards().get(2);

    //Golden card requiring 2 FUNGI + 1 INSECT
    final Card goldCard = assets.getGoldenCards().get(6);
    //Golden card requiring 1 FUNGI and 2 PLANT
    final Card goldCardNotPlaceable = assets.getGoldenCards().get(11);

    @Test
    void placesInitialCard() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);

        Set<PlayArea.Position> curPlayablePos = new HashSet<>();
        curPlayablePos.add(new PlayArea.Position(0,1));
        curPlayablePos.add(new PlayArea.Position(0,-1));
        curPlayablePos.add(new PlayArea.Position(1,0));
        assertNotEquals(curPlayablePos, playArea.getPlayablePositions());
        curPlayablePos.add(new PlayArea.Position(-1,0));
        assertEquals(curPlayablePos, playArea.getPlayablePositions());

        Optional<PlayArea.CardPlacement> optInitialPlacement = playArea.getAt(PlayArea.Position.ORIGIN);
        assertTrue(optInitialPlacement.isPresent());

        PlayArea.CardPlacement cp0 = optInitialPlacement.get();
        assertEquals(playArea, cp0.getPlayArea());
        assertEquals(PlayArea.Position.ORIGIN, cp0.getPosition());
        assertEquals(starterCard, cp0.getCard());
        assertEquals(Side.FRONT, cp0.getSide());
        assertEquals(starterCard.getFace(Side.FRONT), cp0.getVisibleFace());
        assertEquals(0, cp0.getPoints());
    }

    @Test
    void canPlace() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);

        Set<PlayArea.Position> curPlayablePos = new HashSet<>();
        curPlayablePos.add(new PlayArea.Position(0,1));
        curPlayablePos.add(new PlayArea.Position(0,-1));
        curPlayablePos.add(new PlayArea.Position(1,0));
        curPlayablePos.add(new PlayArea.Position(-1,0));
        assertEquals(curPlayablePos, playArea.getPlayablePositions());

        PlayArea.CardPlacement cp1 = playArea.placeAt(1, 0, aCard, Side.BACK);
        curPlayablePos.remove(new PlayArea.Position(1,0));
        curPlayablePos.add(new PlayArea.Position(2,0));
        curPlayablePos.add(new PlayArea.Position(1,1));
        curPlayablePos.add(new PlayArea.Position(1,-1));
        assertEquals(curPlayablePos, playArea.getPlayablePositions());

        assertEquals(playArea, cp1.getPlayArea());
        assertEquals(new PlayArea.Position(1, 0), cp1.getPosition());
        assertEquals(aCard, cp1.getCard());
        assertEquals(Side.BACK, cp1.getSide());
        assertEquals(aCard.getFace(Side.BACK), cp1.getVisibleFace());
        assertEquals(0, cp1.getPoints());
    }

    @Test
    void cantPlaceWithoutConnecting() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        Set<PlayArea.Position> curPlayablePos = new HashSet<>();
        curPlayablePos.add(new PlayArea.Position(0,1));
        curPlayablePos.add(new PlayArea.Position(0,-1));
        curPlayablePos.add(new PlayArea.Position(1,0));
        curPlayablePos.add(new PlayArea.Position(-1,0));
        assertEquals(curPlayablePos, playArea.getPlayablePositions());
        assertThrows(IllegalPlacementException.class, () -> playArea.placeAt(100, 100, aCard, Side.FRONT));
    }

    @Test
    void cantPlaceOverFilledCorner() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);

        Set<PlayArea.Position> curPlayablePos = new HashSet<>();
        curPlayablePos.add(new PlayArea.Position(0,1));
        curPlayablePos.add(new PlayArea.Position(0,-1));
        curPlayablePos.add(new PlayArea.Position(1,0));
        curPlayablePos.add(new PlayArea.Position(-1,0));
        assertEquals(curPlayablePos, playArea.getPlayablePositions());

        playArea.placeAt(1, 0, aCard, Side.FRONT);
        curPlayablePos.remove(new PlayArea.Position(1,0));
        curPlayablePos.add(new PlayArea.Position(2,0));
        curPlayablePos.add(new PlayArea.Position(1,1));
        assertEquals(curPlayablePos, playArea.getPlayablePositions());

        assertThrows(IllegalPlacementException.class, () -> playArea.placeAt(1, -1, aCard, Side.FRONT));
    }

    @Test
    void newPlacementNearMissingCorner() {
        PlayArea playArea = new PlayArea(starterCard, Side.BACK);
        playArea.placeAt(0,1, bCard, Side.FRONT);
        Set<PlayArea.Position> curPlayablePos = new HashSet<>();
        curPlayablePos.add(new PlayArea.Position(0,-1));
        curPlayablePos.add(new PlayArea.Position(1,0));
        curPlayablePos.add(new PlayArea.Position(-1,0));
        curPlayablePos.add(new PlayArea.Position(1,1));
        curPlayablePos.add(new PlayArea.Position(0,2));
        assertEquals(curPlayablePos, playArea.getPlayablePositions());

        playArea.placeAt(-1,0, aCard, Side.FRONT);
        curPlayablePos.remove(new PlayArea.Position(-1,0));
        curPlayablePos.add(new PlayArea.Position(-2,0));
        assertEquals(curPlayablePos, playArea.getPlayablePositions());

        playArea.placeAt(0,-1, cCard, Side.FRONT);
        curPlayablePos.remove(new PlayArea.Position(0,-1));
        curPlayablePos.add(new PlayArea.Position(0,-2));
        assertEquals(curPlayablePos, playArea.getPlayablePositions());
    }

    @Test
    void newPlacementWithMissingCorner() {
        PlayArea playArea = new PlayArea(starterCard, Side.BACK);

        playArea.placeAt(-1,0, aCard, Side.FRONT);
        Set<PlayArea.Position> curPlayablePos = new HashSet<>();
        curPlayablePos.add(new PlayArea.Position(0,-1));
        curPlayablePos.add(new PlayArea.Position(1,0));
        curPlayablePos.add(new PlayArea.Position(0,1));
        curPlayablePos.add(new PlayArea.Position(-2,0));
        curPlayablePos.add(new PlayArea.Position(-1,1));
        assertEquals(curPlayablePos, playArea.getPlayablePositions());

        playArea.placeAt(0,1, bCard, Side.FRONT);
        curPlayablePos.remove(new PlayArea.Position(0,1));
        curPlayablePos.remove(new PlayArea.Position(-1,1));
        curPlayablePos.add(new PlayArea.Position(0,2));
        curPlayablePos.add(new PlayArea.Position(1,1));
        assertEquals(curPlayablePos, playArea.getPlayablePositions());
    }

    @Test
    void getAtIjSameAsGetAtPosition() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        PlayArea.CardPlacement cp = playArea.placeAt(1, 0, aCard, Side.FRONT);

        Set<PlayArea.Position> curPlayablePos = new HashSet<>();
        curPlayablePos.add(new PlayArea.Position(0,1));
        curPlayablePos.add(new PlayArea.Position(0,-1));
        curPlayablePos.add(new PlayArea.Position(-1,0));
        curPlayablePos.add(new PlayArea.Position(2,0));
        curPlayablePos.add(new PlayArea.Position(1,1));
        assertEquals(curPlayablePos, playArea.getPlayablePositions());

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

    @Test
    void placeGoldCard() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        playArea.placeAt(1, 0, aCard, Side.FRONT);
        playArea.placeAt(0, -1, goldCard, Side.FRONT);
        assertThrows(IllegalPlacementException.class, () -> playArea.placeAt(-1, 0, goldCardNotPlaceable, Side.FRONT));
        assertEquals(3, playArea.getScore());
    }

}
