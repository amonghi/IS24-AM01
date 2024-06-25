package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.collectible.Item;
import it.polimi.ingsw.am01.model.exception.IllegalPlacementException;
import it.polimi.ingsw.am01.model.game.GameAssets;
import it.polimi.ingsw.am01.model.game.PlayArea;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DifferentCollectibleObjectiveTest {
    GameAssets assets = GameAssets.getInstance();

    //Objective 13: 3 DIFFERENT ITEMS
    Objective objective = assets.getObjectiveById(13).orElseThrow();
    Card card83 = assets.getStarterCards().get(2);
    //1 QUILL
    Card card5 = assets.getResourceCards().get(4);
    //1 INKWELL
    Card card6 = assets.getResourceCards().get(5);
    //1 MANUSCRIPT
    Card card7 = assets.getResourceCards().get(6);
    //1 QUILL
    Card card15 = assets.getResourceCards().get(14);
    //1 INKWELL
    Card card16 = assets.getResourceCards().get(15);
    //1 MANUSCRIPT
    Card card17 = assets.getResourceCards().get(16);
    //1 QUILL
    Card card25 = assets.getResourceCards().get(24);
    PlayArea pa = new PlayArea(card83, Side.BACK);

    @Test
    void canConstructObjective() {
        DifferentCollectibleObjective obj = new DifferentCollectibleObjective(13, 3, Set.of(Item.INKWELL, Item.MANUSCRIPT, Item.QUILL));
        assertEquals(objective, obj);
    }

    @Test
    void noItems() {
        assertEquals(0, objective.getEarnedPoints(pa));
    }
    @Test
    void oneItem() throws IllegalPlacementException {
        pa.placeAt(1,0,card25,Side.FRONT);
        assertEquals(0, objective.getEarnedPoints(pa));
    }

    @Test
    void threeIdenticalItems() throws IllegalPlacementException {
        pa.placeAt(1,0,card25,Side.FRONT);
        pa.placeAt(0,-1,card16,Side.FRONT);
        pa.placeAt(-1,0,card6,Side.FRONT);
        assertEquals(0, objective.getEarnedPoints(pa));
    }

    @Test
    void twoOutOfThree() throws IllegalPlacementException {
        pa.placeAt(1,0,card16,Side.FRONT);
        pa.placeAt(2,0,card5,Side.FRONT);
        assertEquals(0, objective.getEarnedPoints(pa));
    }
    @Test
    void completeSet() throws IllegalPlacementException {
        pa.placeAt(0,1,card16,Side.FRONT);
        pa.placeAt(0,-1,card7,Side.FRONT);
        pa.placeAt(-1,0,card5,Side.FRONT);
        assertEquals(3, objective.getEarnedPoints(pa));
    }
    @Test
    void oneExceedingElement() throws IllegalPlacementException {
        pa.placeAt(1,0,card6,Side.FRONT);
        pa.placeAt(-1,0,card5,Side.FRONT);
        pa.placeAt(0,1,card16,Side.FRONT);
        pa.placeAt(0,-1,card7,Side.FRONT);
        assertEquals(3, objective.getEarnedPoints(pa));
    }

    @Test
    void twoExceedingElements() throws IllegalPlacementException {
        pa.placeAt(1,0,card6,Side.FRONT);
        pa.placeAt(-1,0,card5,Side.FRONT);
        pa.placeAt(0,1,card16,Side.FRONT);
        pa.placeAt(0,-1,card7,Side.FRONT);
        pa.placeAt(0,-2,card15,Side.FRONT);
        assertEquals(3, objective.getEarnedPoints(pa));
    }

    @Test
    void twoCompleteSet() throws IllegalPlacementException {
        pa.placeAt(1,0,card6,Side.FRONT);
        pa.placeAt(-1,0,card5,Side.FRONT);
        pa.placeAt(0,1,card16,Side.FRONT);
        pa.placeAt(0,-1,card7,Side.FRONT);
        pa.placeAt(0,-2,card15,Side.FRONT);
        pa.placeAt(-2,0,card17,Side.FRONT);
        assertEquals(6, objective.getEarnedPoints(pa));
    }

}