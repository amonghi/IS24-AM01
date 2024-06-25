package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.exception.IllegalPlacementException;
import it.polimi.ingsw.am01.model.game.GameAssets;
import it.polimi.ingsw.am01.model.game.PlayArea;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SameCollectibleObjectiveTest {

    //GameAsset
    GameAssets assets = GameAssets.getInstance();

    //Objective 12: 3 INSECTS
    Objective objectiveResources = assets.getObjectiveById(12).orElseThrow();

    Card card83 = assets.getStarterCards().get(2);
    Card card15 = assets.getResourceCards().get(14);
    Card card31 = assets.getResourceCards().get(30);
    Card card32 = assets.getResourceCards().get(31);
    Card card33 = assets.getResourceCards().get(32);
    Card card34 = assets.getResourceCards().get(33);

    //Define the PlayArea
    PlayArea pa = new PlayArea(card83, Side.FRONT);

    @Test
    void canConstructObjective() {
        SameCollectibleObjective obj = new SameCollectibleObjective(12, 3, Resource.INSECT, 3);
        assertEquals(objectiveResources, obj);
    }

    @Test
    void noSufficientResources() throws IllegalPlacementException {
        pa.placeAt(1,0, card31, Side.BACK);
        assertEquals(0, objectiveResources.getEarnedPoints(pa));
    }
    @Test
    void exactlyOneMatch() throws IllegalPlacementException {
        pa.placeAt(1,0, card34, Side.BACK);
        pa.placeAt(1,-1, card15, Side.FRONT);
        assertEquals(2, objectiveResources.getEarnedPoints(pa));
    }
    @Test
    void oneMatch() throws IllegalPlacementException {
        pa.placeAt(1,0, card31, Side.BACK);
        pa.placeAt(1,1, card32, Side.BACK);
        pa.placeAt(1,-1,card34,Side.FRONT);
        assertEquals(2, objectiveResources.getEarnedPoints(pa));
    }
    @Test
    void moreMatches() throws IllegalPlacementException {
        pa.placeAt(1,0, card32, Side.BACK);
        pa.placeAt(1,1, card31, Side.FRONT);
        pa.placeAt(1,-1,card34,Side.FRONT);
        pa.placeAt(0,1, card33, Side.FRONT);
        assertEquals(4, objectiveResources.getEarnedPoints(pa));
    }

}