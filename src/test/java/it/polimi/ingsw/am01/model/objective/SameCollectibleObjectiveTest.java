package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.GameAssets;
import it.polimi.ingsw.am01.model.game.PlayArea;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SameCollectibleObjectiveTest {

    //GameAsset
    GameAssets assets = GameAssets.getInstance();

    //Objective 11: 3 INSECTS
    Objective objectiveResources = assets.getObjectives().get(11);

    Card card83 = assets.getStarterCards().get(2);
    Card card15 = assets.getResourceCards().get(14);
    Card card31 = assets.getResourceCards().get(30);
    Card card32 = assets.getResourceCards().get(31);
    Card card33 = assets.getResourceCards().get(32);
    Card card34 = assets.getResourceCards().get(33);

    //Define the PlayArea
    PlayArea pa = new PlayArea(card83, Side.FRONT);

    @Test
    void noSufficientResources() {
        pa.placeAt(1,0, card31, Side.BACK);
        assertEquals(0, objectiveResources.getEarnedPoints(pa));
    }
    @Test
    void exactlyOneMatch() {
        pa.placeAt(1,0, card34, Side.BACK);
        pa.placeAt(1,-1, card15, Side.FRONT);
        assertEquals(2, objectiveResources.getEarnedPoints(pa));
    }
    @Test
    void oneMatch() {
        pa.placeAt(1,0, card31, Side.BACK);
        pa.placeAt(1,1, card32, Side.BACK);
        pa.placeAt(1,-1,card34,Side.FRONT);
        assertEquals(2, objectiveResources.getEarnedPoints(pa));
    }
    @Test
    void moreMatches() {
        pa.placeAt(1,0, card32, Side.BACK);
        pa.placeAt(1,1, card31, Side.FRONT);
        pa.placeAt(1,-1,card34,Side.FRONT);
        pa.placeAt(0,1, card33, Side.FRONT);
        assertEquals(4, objectiveResources.getEarnedPoints(pa));
    }

}