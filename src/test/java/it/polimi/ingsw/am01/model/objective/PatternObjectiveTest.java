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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PatternObjectiveTest {

    GameAssets assets = GameAssets.getInstance();
    //Objective 4: SEQUENCE OF 3 INSECT CARDS
    Objective objectiveInsect = assets.getObjectives().get(3);

    //for simplicity, I build cards based only on color
    FrontCardFace front = new FrontCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty()
    );
    BackCardFace backInsect = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<>(Map.of(Resource.INSECT, 1))
    );
    BackCardFace backFungi = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<>(Map.of(Resource.FUNGI, 1))
    );
    BackCardFace backAnimal = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<>(Map.of(Resource.ANIMAL, 1))
    );
    BackCardFace backPlant = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<>(Map.of(Resource.PLANT, 1))
    );
    Card insectCard = new Card(1, CardColor.PURPLE, false, false, front, backInsect);
    Card fungiCard = new Card(2, CardColor.RED, false,false, front, backFungi);
    Card animalCard = new Card(3,CardColor.BLUE, false, true, front, backAnimal);
    Card plantCard = new Card(4, CardColor.GREEN, false, false, front, backPlant);
    Card startedCard = new Card(5, CardColor.NEUTRAL, true, false, front, backFungi);

    //TESTS FOR "SEQUENCE PATTERN", ref. card 90
    @Test
    void noMatchesSequence() {
        PlayArea pa = new PlayArea(startedCard, Side.FRONT);
        pa.placeAt(0,1, insectCard, Side.FRONT);
        pa.placeAt(1,0, insectCard, Side.FRONT);
        pa.placeAt(2,0, insectCard, Side.FRONT);
        pa.placeAt(1,-1, insectCard, Side.FRONT);
        pa.placeAt(1,-2, fungiCard, Side.FRONT);
        assertEquals(0, objectiveInsect.getEarnedPoints(pa));
    }
    @Test
    void oneMatchSequence() {
        PlayArea pa = new PlayArea(startedCard, Side.FRONT);
        pa.placeAt(0,1, insectCard, Side.FRONT);
        pa.placeAt(1,0, insectCard, Side.FRONT);
        pa.placeAt(1,1, insectCard, Side.FRONT);
        pa.placeAt(2,0, insectCard, Side.FRONT);
        pa.placeAt(1,-1, insectCard, Side.FRONT);
        pa.placeAt(3,0, insectCard, Side.FRONT);
        pa.placeAt(1,-2, fungiCard, Side.FRONT);
        assertEquals(2, objectiveInsect.getEarnedPoints(pa));
    }

    @Test
    void overlapSequence() {
        PlayArea pa = new PlayArea(startedCard, Side.FRONT);
        pa.placeAt(0,1, insectCard, Side.FRONT);
        pa.placeAt(1,0, insectCard, Side.BACK);
        pa.placeAt(1,1, insectCard, Side.FRONT);
        pa.placeAt(2,0, insectCard, Side.FRONT);
        pa.placeAt(1,-1, insectCard, Side.FRONT);
        pa.placeAt(3,0, insectCard, Side.FRONT);
        pa.placeAt(1,-2, insectCard, Side.FRONT);
        pa.placeAt(3,1, insectCard, Side.FRONT);
        assertEquals(2, objectiveInsect.getEarnedPoints(pa));
    }

    @Test
    void twoDistinctMatchesSequence() {
        PlayArea pa = new PlayArea(startedCard, Side.FRONT);
        pa.placeAt(0,1, insectCard, Side.FRONT);
        pa.placeAt(1,0, insectCard, Side.BACK);
        pa.placeAt(1,1, insectCard, Side.FRONT);
        pa.placeAt(2,0, insectCard, Side.FRONT);
        pa.placeAt(1,-1, insectCard, Side.FRONT);
        pa.placeAt(3,0, insectCard, Side.FRONT);
        pa.placeAt(3,-1, insectCard, Side.BACK);
        pa.placeAt(1,-2, insectCard, Side.FRONT);
        pa.placeAt(3,1, insectCard, Side.FRONT);
        assertEquals(4, objectiveInsect.getEarnedPoints(pa));
    }

    @Test
    void twoOverlappingMatchesSequence() {
        PlayArea pa = new PlayArea(startedCard, Side.FRONT);
        pa.placeAt(0,1, insectCard, Side.FRONT);
        pa.placeAt(1,0, insectCard, Side.BACK);
        pa.placeAt(1,1, insectCard, Side.FRONT);
        pa.placeAt(2,0, insectCard, Side.FRONT);
        pa.placeAt(1,-1, insectCard, Side.FRONT);
        pa.placeAt(3,0, insectCard, Side.FRONT);
        pa.placeAt(3,-1, insectCard, Side.BACK);
        pa.placeAt(1,-2, insectCard, Side.FRONT);
        pa.placeAt(3,1, insectCard, Side.FRONT);
        pa.placeAt(1,-3,insectCard, Side.FRONT);
        pa.placeAt(1,-4, insectCard, Side.BACK);
        assertEquals(6, objectiveInsect.getEarnedPoints(pa));
    }

    //TESTS FOR "L PATTERN", ref card 93
    Objective objectiveL = assets.getObjectives().get(6);

    @Test
    void noMatchL() {
        PlayArea pa = new PlayArea(startedCard, Side.BACK);
        pa.placeAt(0,1, fungiCard, Side.FRONT);
        pa.placeAt(1,0, animalCard, Side.BACK);
        pa.placeAt(0,-1, fungiCard, Side.BACK);
        pa.placeAt(2,0, fungiCard, Side.FRONT);
        assertEquals(0, objectiveL.getEarnedPoints(pa));
    }

    @Test
    void oneMatchL() {
        PlayArea pa = new PlayArea(startedCard, Side.FRONT);
        pa.placeAt(0,1, fungiCard, Side.FRONT);
        pa.placeAt(1,0, animalCard, Side.BACK);
        pa.placeAt(0,-1, animalCard, Side.FRONT);
        pa.placeAt(2,0, fungiCard, Side.BACK);
        assertEquals(3, objectiveL.getEarnedPoints(pa));
    }

    @Test
    void twoUseOfSameCard() {
        PlayArea pa = new PlayArea(startedCard, Side.FRONT);
        pa.placeAt(0,1, fungiCard, Side.FRONT);
        pa.placeAt(1,0, animalCard, Side.BACK);
        pa.placeAt(1,-1, fungiCard, Side.FRONT);
        pa.placeAt(0,-1, animalCard, Side.FRONT);
        pa.placeAt(-1,-1, insectCard, Side.BACK);
        pa.placeAt(2,0, fungiCard, Side.BACK);
        pa.placeAt(-1,-2, animalCard, Side.FRONT);
        assertEquals(3, objectiveL.getEarnedPoints(pa));
    }
    @Test
    void twoMatches() {
        PlayArea pa = new PlayArea(startedCard, Side.FRONT);
        pa.placeAt(0,1, animalCard, Side.BACK);
        pa.placeAt(1,1,fungiCard, Side.FRONT);
        pa.placeAt(1,0, animalCard, Side.BACK);
        pa.placeAt(1,-1, fungiCard, Side.FRONT);
        pa.placeAt(0,-1, animalCard, Side.FRONT);
        pa.placeAt(-1,-1, insectCard, Side.BACK);
        pa.placeAt(-1,0, animalCard, Side.FRONT);
        pa.placeAt(2,0, fungiCard, Side.BACK);
        pa.placeAt(-1,-2, animalCard, Side.FRONT);
        assertEquals(6, objectiveL.getEarnedPoints(pa));
    }

    @Test
    void twoConsecutiveMatches() {
        PlayArea pa = new PlayArea(startedCard, Side.FRONT);
        pa.placeAt(0,1, animalCard, Side.BACK);
        pa.placeAt(1,1,fungiCard, Side.FRONT);
        pa.placeAt(1,0, animalCard, Side.BACK);
        pa.placeAt(1,-1, fungiCard, Side.FRONT);
        pa.placeAt(0,-1, animalCard, Side.FRONT);
        pa.placeAt(-1,-1, insectCard, Side.BACK);
        pa.placeAt(-1,-2, animalCard, Side.FRONT);
        pa.placeAt(-2,-2, animalCard, Side.FRONT);
        pa.placeAt(-1,0, animalCard, Side.FRONT);
        pa.placeAt(2,0, fungiCard, Side.BACK);
        pa.placeAt(-2,-3, animalCard, Side.FRONT);
        pa.placeAt(0,-2, fungiCard, Side.FRONT);
        assertEquals(9, objectiveL.getEarnedPoints(pa));
    }

    //TEST FOR EACH PATTERN: one match only
    //Pattern 87 identical to pattern 89
    //Pattern 88 identical to pattern 90 (ALREADY TESTED)
    //L pattern have different relative positions, pattern 93 ALREADY TESTED

    @Test
    void pattern87() {
        Objective objective = assets.getObjectives().get(0);

        PlayArea pa = new PlayArea(startedCard, Side.FRONT);
        pa.placeAt(0,1, fungiCard, Side.BACK);
        pa.placeAt(0,-1, fungiCard, Side.FRONT);
        pa.placeAt(1,0, fungiCard, Side.FRONT);
        pa.placeAt(1,1, fungiCard, Side.BACK);
        pa.placeAt(2,0, fungiCard, Side.FRONT);
        pa.placeAt(2,1, fungiCard, Side.FRONT);
        assertEquals(2, objective.getEarnedPoints(pa));
    }

    @Test
    void pattern91() {
        Objective objective = assets.getObjectives().get(4);

        PlayArea pa = new PlayArea(startedCard, Side.FRONT);
        pa.placeAt(0,-1, fungiCard, Side.FRONT);
        pa.placeAt(1,-1, plantCard, Side.BACK);
        pa.placeAt(1,0, fungiCard, Side.FRONT);
        pa.placeAt(2,0, plantCard, Side.FRONT);
        pa.placeAt(2,1, fungiCard, Side.FRONT);
        pa.placeAt(3,1, plantCard, Side.BACK);
        assertEquals(3, objective.getEarnedPoints(pa));
    }

    @Test
    void pattern92() {
        Objective objective = assets.getObjectives().get(5);

        PlayArea pa = new PlayArea(startedCard, Side.FRONT);
        pa.placeAt(0,-1, insectCard, Side.BACK);
        pa.placeAt(1,-1, plantCard, Side.BACK);
        pa.placeAt(1,0, insectCard, Side.BACK);
        pa.placeAt(2,0, plantCard, Side.BACK);
        pa.placeAt(2,1, animalCard, Side.BACK);
        pa.placeAt(3,1, plantCard, Side.BACK);
        assertEquals(3, objective.getEarnedPoints(pa));
    }

    @Test
    void pattern94() {
        Objective objective = assets.getObjectives().get(7);

        PlayArea pa = new PlayArea(startedCard, Side.FRONT);
        pa.placeAt(0,1, insectCard, Side.FRONT);
        pa.placeAt(-1,0, insectCard, Side.FRONT);
        pa.placeAt(0,2, animalCard, Side.FRONT);
        pa.placeAt(-1,1, animalCard, Side.FRONT);
        pa.placeAt(-2,0, fungiCard, Side.FRONT);
        assertEquals(3, objective.getEarnedPoints(pa));
    }
}