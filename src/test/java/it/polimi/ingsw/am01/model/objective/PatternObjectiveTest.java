package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.game.GameAssets;
import it.polimi.ingsw.am01.model.game.PlayArea;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PatternObjectiveTest {

    GameAssets assets = GameAssets.getInstance();
    //Objective 4: SEQUENCE OF 3 INSECT CARDS
    Objective objectiveInsect = assets.getObjectives().get(3);
    List<Card> resourceCards = assets.getResourceCards();
    List<Card> goldenCards = assets.getGoldenCards();
    Card starterCard = assets.getStarterCards().get(0);

    //TESTS FOR "SEQUENCE PATTERN", ref. card 90
    @Test
    void noMatchSequence() {
        PlayArea pa = new PlayArea(starterCard, Side.FRONT);
        pa.placeAt(0,1, resourceCards.get(10), Side.FRONT);
        pa.placeAt(1,0, resourceCards.get(35), Side.FRONT);
        pa.placeAt(2,0, resourceCards.get(33), Side.FRONT);
        pa.placeAt(1,-1, resourceCards.get(37), Side.BACK);
        pa.placeAt(1,-2, resourceCards.get(3), Side.FRONT);
        assertEquals(0, objectiveInsect.getEarnedPoints(pa));
    }
    @Test
    void oneMatchSequence() {
        PlayArea pa = new PlayArea(starterCard, Side.FRONT);
        pa.placeAt(0,1, resourceCards.get(10), Side.FRONT);
        pa.placeAt(1,0, resourceCards.get(35), Side.FRONT);
        pa.placeAt(1,1, resourceCards.get(31), Side.FRONT);
        pa.placeAt(2,0, resourceCards.get(33), Side.FRONT);
        pa.placeAt(1,-1, resourceCards.get(37), Side.FRONT);
        pa.placeAt(3,0, resourceCards.get(34), Side.FRONT);
        pa.placeAt(1,-2, resourceCards.get(3), Side.FRONT);
        assertEquals(2, objectiveInsect.getEarnedPoints(pa));
    }

    @Test
    void overlapSequence() {
        PlayArea pa = new PlayArea(starterCard, Side.FRONT);
        pa.placeAt(0,1, resourceCards.get(10), Side.FRONT);
        pa.placeAt(1,0, resourceCards.get(35), Side.BACK);
        pa.placeAt(1,1, resourceCards.get(31), Side.FRONT);
        pa.placeAt(2,0, resourceCards.get(33), Side.FRONT);
        pa.placeAt(1,-1, resourceCards.get(37), Side.FRONT);
        pa.placeAt(3,0, resourceCards.get(34), Side.BACK);
        pa.placeAt(1,-2, resourceCards.get(38), Side.FRONT);
        pa.placeAt(3,1, resourceCards.get(39), Side.FRONT);
        assertEquals(2, objectiveInsect.getEarnedPoints(pa));
    }

    @Test
    void twoDistinctMatchesSequence() {
        PlayArea pa = new PlayArea(starterCard, Side.FRONT);
        pa.placeAt(0,1, resourceCards.get(10), Side.FRONT);
        pa.placeAt(1,0, resourceCards.get(35), Side.BACK);
        pa.placeAt(1,1, resourceCards.get(31), Side.FRONT);
        pa.placeAt(2,0, resourceCards.get(3), Side.FRONT);
        pa.placeAt(1,-1, resourceCards.get(37), Side.FRONT);
        pa.placeAt(3,0, resourceCards.get(33), Side.FRONT);
        pa.placeAt(3,-1, resourceCards.get(32), Side.BACK);
        pa.placeAt(1,-2, resourceCards.get(38), Side.FRONT);
        pa.placeAt(3,1, resourceCards.get(34), Side.FRONT);
        assertEquals(4, objectiveInsect.getEarnedPoints(pa));
    }

    @Test
    void twoOverlappingMatchesSequence() {
        PlayArea pa = new PlayArea(starterCard, Side.FRONT);
        pa.placeAt(0,1, resourceCards.get(10), Side.FRONT);
        pa.placeAt(1,0, resourceCards.get(35), Side.BACK);
        pa.placeAt(1,1, resourceCards.get(31), Side.FRONT);
        pa.placeAt(2,0, resourceCards.get(3), Side.FRONT);
        pa.placeAt(1,-1, resourceCards.get(37), Side.FRONT);
        pa.placeAt(3,0, resourceCards.get(33), Side.FRONT);
        pa.placeAt(3,-1, resourceCards.get(32), Side.BACK);
        pa.placeAt(1,-2, resourceCards.get(38), Side.FRONT);
        pa.placeAt(3,1, resourceCards.get(34), Side.FRONT);
        pa.placeAt(1,-3, resourceCards.get(39), Side.FRONT);
        pa.placeAt(1,-4, resourceCards.get(30), Side.BACK);
        assertEquals(6, objectiveInsect.getEarnedPoints(pa));
    }

    //TESTS FOR "L PATTERN", ref card 93
    Objective objectiveL = assets.getObjectives().get(6);

    @Test
    void noMatchL() {
        PlayArea pa = new PlayArea(starterCard, Side.BACK);
        pa.placeAt(0,1, resourceCards.get(1), Side.FRONT);
        pa.placeAt(1,0, resourceCards.get(21), Side.BACK);
        pa.placeAt(0,-1, resourceCards.get(8), Side.BACK);
        pa.placeAt(2,0, resourceCards.get(6), Side.FRONT);
        assertEquals(0, objectiveL.getEarnedPoints(pa));
    }

    @Test
    void oneMatchL() {
        PlayArea pa = new PlayArea(starterCard, Side.FRONT);
        pa.placeAt(0,1, resourceCards.get(1), Side.FRONT);
        pa.placeAt(1,0, resourceCards.get(21), Side.BACK);
        pa.placeAt(0,-1, resourceCards.get(22), Side.FRONT);
        pa.placeAt(2,0, resourceCards.get(6), Side.BACK);
        assertEquals(3, objectiveL.getEarnedPoints(pa));
    }

    @Test
    void twoUseOfSameCard() {
        PlayArea pa = new PlayArea(starterCard, Side.FRONT);
        pa.placeAt(0,1, resourceCards.get(1), Side.FRONT);
        pa.placeAt(1,0, resourceCards.get(21), Side.BACK);
        pa.placeAt(1,-1, resourceCards.get(8), Side.FRONT);
        pa.placeAt(0,-1, resourceCards.get(20), Side.FRONT);
        pa.placeAt(-1,-1, resourceCards.get(31), Side.BACK);
        pa.placeAt(2,0, resourceCards.get(6), Side.BACK);
        pa.placeAt(-1,-2, goldenCards.get(22), Side.FRONT);
        assertEquals(3, objectiveL.getEarnedPoints(pa));
    }
    @Test
    void twoMatches() {
        PlayArea pa = new PlayArea(starterCard, Side.FRONT);
        pa.placeAt(0,1, resourceCards.get(20), Side.BACK);
        pa.placeAt(1,1,resourceCards.get(1), Side.FRONT);
        pa.placeAt(1,0, resourceCards.get(23), Side.BACK);
        pa.placeAt(1,-1, resourceCards.get(8), Side.FRONT);
        pa.placeAt(0,-1, resourceCards.get(24), Side.FRONT);
        pa.placeAt(-1,-1, resourceCards.get(32), Side.BACK);
        pa.placeAt(-1,0, resourceCards.get(27), Side.FRONT);
        pa.placeAt(2,0, resourceCards.get(6), Side.BACK);
        pa.placeAt(-1,-2, resourceCards.get(26), Side.FRONT);
        assertEquals(6, objectiveL.getEarnedPoints(pa));
    }

    @Test
    void twoConsecutiveMatches() {
        PlayArea pa = new PlayArea(starterCard, Side.FRONT);
        pa.placeAt(0,1, resourceCards.get(20), Side.BACK);
        pa.placeAt(1,1,resourceCards.get(1), Side.FRONT);
        pa.placeAt(1,0, resourceCards.get(23), Side.BACK);
        pa.placeAt(1,-1, resourceCards.get(8), Side.FRONT);
        pa.placeAt(0,-1, resourceCards.get(24), Side.FRONT);
        pa.placeAt(-1,-1, resourceCards.get(33), Side.BACK);
        pa.placeAt(-1,-2, resourceCards.get(27), Side.FRONT);
        pa.placeAt(-2,-2, resourceCards.get(25), Side.FRONT);
        pa.placeAt(-1,0, resourceCards.get(26), Side.FRONT);
        pa.placeAt(2,0, resourceCards.get(6), Side.BACK);
        pa.placeAt(-2,-3, resourceCards.get(29), Side.FRONT);
        pa.placeAt(0,-2, resourceCards.get(0), Side.FRONT);
        assertEquals(9, objectiveL.getEarnedPoints(pa));
    }

    //TEST FOR EACH PATTERN: one match only
    //Pattern 87 identical to pattern 89
    //Pattern 88 identical to pattern 90 (ALREADY TESTED)
    //L pattern have different relative positions, pattern 93 ALREADY TESTED

    @Test
    void pattern87() {
        Objective objective = assets.getObjectives().get(0);

        PlayArea pa = new PlayArea(starterCard, Side.FRONT);
        pa.placeAt(0,1, resourceCards.get(0), Side.FRONT);
        pa.placeAt(0,-1, resourceCards.get(1), Side.FRONT);
        pa.placeAt(1,0, resourceCards.get(7), Side.FRONT);
        pa.placeAt(1,1, resourceCards.get(3), Side.BACK);
        pa.placeAt(2,0, resourceCards.get(5), Side.FRONT);
        pa.placeAt(2,1, goldenCards.get(9), Side.FRONT);
        assertEquals(2, objective.getEarnedPoints(pa));
    }

    @Test
    void pattern91() {
        Objective objective = assets.getObjectives().get(4);

        PlayArea pa = new PlayArea(starterCard, Side.FRONT);
        pa.placeAt(0,-1, resourceCards.get(5), Side.FRONT);
        pa.placeAt(1,-1, resourceCards.get(17), Side.BACK);
        pa.placeAt(1,0, resourceCards.get(3), Side.FRONT);
        pa.placeAt(2,0, resourceCards.get(12), Side.FRONT);
        pa.placeAt(2,1, resourceCards.get(7), Side.FRONT);
        pa.placeAt(3,1, resourceCards.get(19), Side.BACK);
        assertEquals(3, objective.getEarnedPoints(pa));
    }

    @Test
    void pattern92() {
        Objective objective = assets.getObjectives().get(5);

        PlayArea pa = new PlayArea(starterCard, Side.FRONT);
        pa.placeAt(0,-1, resourceCards.get(30), Side.BACK);
        pa.placeAt(1,-1, resourceCards.get(12), Side.BACK);
        pa.placeAt(1,0, resourceCards.get(38), Side.BACK);
        pa.placeAt(2,0, resourceCards.get(18), Side.BACK);
        pa.placeAt(2,1, resourceCards.get(34), Side.BACK);
        pa.placeAt(3,1, resourceCards.get(19), Side.BACK);
        assertEquals(3, objective.getEarnedPoints(pa));
    }

    @Test
    void pattern94() {
        Objective objective = assets.getObjectives().get(7);

        PlayArea pa = new PlayArea(starterCard, Side.FRONT);
        pa.placeAt(0,1, resourceCards.get(37), Side.FRONT);
        pa.placeAt(-1,0, resourceCards.get(36), Side.FRONT);
        pa.placeAt(0,2, resourceCards.get(27), Side.FRONT);
        pa.placeAt(-1,1, resourceCards.get(20), Side.FRONT);
        pa.placeAt(-2,0, resourceCards.get(8), Side.FRONT);
        assertEquals(3, objective.getEarnedPoints(pa));
    }
}