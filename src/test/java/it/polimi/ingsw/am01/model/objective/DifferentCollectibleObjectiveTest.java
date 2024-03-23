package it.polimi.ingsw.am01.model.objective;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.collectible.Item;
import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.game.PlayArea;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DifferentCollectibleObjectiveTest {
    //Card 83 as started card
    FrontCardFace frontCard83 = new FrontCardFace(
            Corner.empty(),
            Corner.filled(Resource.INSECT),
            Corner.filled(Resource.PLANT),
            Corner.filled(Resource.FUNGI)
    );
    BackCardFace backCard83 = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<Resource, Integer>(
                    Map.of(
                            Resource.PLANT, 1,
                            Resource.FUNGI, 1)
            )
    );
    Card card83 = new Card(CardColor.NEUTRAL, true, false, frontCard83, backCard83);

    //Card 5: 1 QUILL
    FrontCardFace frontCard5 = new FrontCardFace(
            Corner.filled(Item.QUILL),
            Corner.missing(),
            Corner.filled(Resource.FUNGI),
            Corner.filled(Resource.PLANT)
    );
    BackCardFace backCard5 = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<Resource, Integer>(Map.of(Resource.FUNGI, 1))
    );
    Card card5 = new Card(CardColor.RED, false, false, frontCard5, backCard5);

    //Card 6: 1 INKWELL
    FrontCardFace frontCard6 = new FrontCardFace(
            Corner.filled(Resource.FUNGI),
            Corner.filled(Item.INKWELL),
            Corner.filled(Resource.ANIMAL),
            Corner.missing()
    );
    BackCardFace backCard6 = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<Resource, Integer>(Map.of(Resource.FUNGI, 1))
    );
    Card card6 = new Card(CardColor.RED, false, false, frontCard6, backCard6);

    //Card 7: 1 MANUSCRIPT
    FrontCardFace frontCard7 = new FrontCardFace(
            Corner.filled(Resource.INSECT),
            Corner.filled(Resource.FUNGI),
            Corner.empty(),
            Corner.filled(Item.MANUSCRIPT)
    );
    BackCardFace backCard7 = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<Resource, Integer>(Map.of(Resource.FUNGI, 1))
    );
    Card card7 = new Card(CardColor.RED, false, false, frontCard7, backCard7);

    //Card 15: 1 QUILL
    FrontCardFace frontCard15 = new FrontCardFace(
            Corner.filled(Resource.INSECT),
            Corner.missing(),
            Corner.filled(Resource.PLANT),
            Corner.filled(Item.QUILL)
    );
    BackCardFace backCard15 = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<Resource, Integer>(Map.of(Resource.PLANT, 1))
    );
    Card card15 = new Card(CardColor.GREEN, false, false, frontCard15, backCard15);

    //Card 16: 1 INKWELL
    FrontCardFace frontCard16 = new FrontCardFace(
            Corner.filled(Resource.PLANT),
            Corner.filled(Resource.FUNGI),
            Corner.filled(Item.INKWELL),
            Corner.missing()
    );
    BackCardFace backCard16 = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<Resource, Integer>(Map.of(Resource.PLANT, 1))
    );
    Card card16 = new Card(CardColor.GREEN, false, false, frontCard16, backCard16);

    //Card 17: 1 MANUSCRIPT
    FrontCardFace frontCard17 = new FrontCardFace(
            Corner.missing(),
            Corner.filled(Item.MANUSCRIPT),
            Corner.filled(Resource.ANIMAL),
            Corner.filled(Resource.PLANT)
    );
    BackCardFace backCard17 = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<Resource, Integer>(Map.of(Resource.PLANT, 1))
    );
    Card card17 = new Card(CardColor.GREEN, false, false, frontCard17, backCard17);

    //Card 25: 1 INKWELL
    FrontCardFace frontCard25 = new FrontCardFace(
            Corner.filled(Resource.INSECT),
            Corner.missing(),
            Corner.filled(Resource.ANIMAL),
            Corner.filled(Item.INKWELL)
    );
    BackCardFace backCard25 = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<Resource, Integer>(Map.of(Resource.ANIMAL, 1))
    );
    Card card25 = new Card(CardColor.BLUE, false, false, frontCard25, backCard25);

    PlayArea pa = new PlayArea(card83, Side.BACK);
    Set<Item> requiredItems = new HashSet(Set.of(Item.INKWELL, Item.QUILL, Item.MANUSCRIPT));
    Objective objective = new DifferentCollectibleObjective(3, requiredItems);

    @Test
    void noItems() {
        assertEquals(0, objective.getEarnedPoints(pa));
    }
    @Test
    void oneItem() {
        pa.placeAt(1,0,card25,Side.FRONT);
        assertEquals(0, objective.getEarnedPoints(pa));
    }

    @Test
    void threeIdenticalItems() {
        pa.placeAt(1,0,card25,Side.FRONT);
        pa.placeAt(0,-1,card16,Side.FRONT);
        pa.placeAt(-1,0,card6,Side.FRONT);
        assertEquals(0, objective.getEarnedPoints(pa));
    }

    @Test
    void twoOutOfThree() {
        pa.placeAt(1,0,card16,Side.FRONT);
        pa.placeAt(2,0,card5,Side.FRONT);
        assertEquals(0, objective.getEarnedPoints(pa));
    }
    @Test
    void completSet() {
        pa.placeAt(0,1,card16,Side.FRONT);
        pa.placeAt(0,-1,card7,Side.FRONT);
        pa.placeAt(-1,0,card5,Side.FRONT);
        assertEquals(3, objective.getEarnedPoints(pa));
    }
    @Test
    void oneExceedingElement() {
        pa.placeAt(1,0,card6,Side.FRONT);
        pa.placeAt(-1,0,card5,Side.FRONT);
        pa.placeAt(0,1,card16,Side.FRONT);
        pa.placeAt(0,-1,card7,Side.FRONT);
        assertEquals(3, objective.getEarnedPoints(pa));
    }

    @Test
    void twoExceedingElements() {
        pa.placeAt(1,0,card6,Side.FRONT);
        pa.placeAt(-1,0,card5,Side.FRONT);
        pa.placeAt(0,1,card16,Side.FRONT);
        pa.placeAt(0,-1,card7,Side.FRONT);
        pa.placeAt(0,-2,card15,Side.FRONT);
        assertEquals(3, objective.getEarnedPoints(pa));
    }

    @Test
    void twoCompleteSet() {
        pa.placeAt(1,0,card6,Side.FRONT);
        pa.placeAt(-1,0,card5,Side.FRONT);
        pa.placeAt(0,1,card16,Side.FRONT);
        pa.placeAt(0,-1,card7,Side.FRONT);
        pa.placeAt(0,-2,card15,Side.FRONT);
        pa.placeAt(-2,0,card17,Side.FRONT);
        assertEquals(6, objective.getEarnedPoints(pa));
    }

}