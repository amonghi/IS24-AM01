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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SameCollectibleObjectiveTest {
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
    Card card83 = new Card(83, CardColor.NEUTRAL, true, false, frontCard83, backCard83);

    //Card 15
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
    Card card15 = new Card(15, CardColor.GREEN, false, false, frontCard15, backCard15);

    //Card 31
    FrontCardFace frontCard31 = new FrontCardFace(
            Corner.filled(Resource.INSECT),
            Corner.filled(Resource.INSECT),
            Corner.missing(),
            Corner.empty()
    );
    BackCardFace backCard31 = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<Resource, Integer>(Map.of(Resource.INSECT, 1))
    );
    Card card31 = new Card(31, CardColor.PURPLE, false, false, frontCard31, backCard31);

    //Card 32
    FrontCardFace frontCard32 = new FrontCardFace(
            Corner.empty(),
            Corner.missing(),
            Corner.filled(Resource.INSECT),
            Corner.filled(Resource.INSECT)
    );
    BackCardFace backCard32 = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<Resource, Integer>(Map.of(Resource.INSECT, 1))
    );
    Card card32 = new Card(32, CardColor.PURPLE, false, false, frontCard32, backCard32);

    //Card 33
    FrontCardFace frontCard33 = new FrontCardFace(
            Corner.missing(),
            Corner.filled(Resource.INSECT),
            Corner.empty(),
            Corner.filled(Resource.INSECT)
    );
    BackCardFace backCard33 = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<Resource, Integer>(Map.of(Resource.INSECT, 1))
    );
    Card card33 = new Card(33, CardColor.PURPLE, false, false, frontCard33, backCard33);

    //Card 34
    FrontCardFace frontCard34 = new FrontCardFace(
            Corner.filled(Resource.INSECT),
            Corner.empty(),
            Corner.filled(Resource.INSECT),
            Corner.missing()
    );
    BackCardFace backCard34 = new BackCardFace(
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            Corner.empty(),
            new HashMap<Resource, Integer>(Map.of(Resource.INSECT, 1))
    );
    Card card34 = new Card(34, CardColor.PURPLE, false, false, frontCard34, backCard34);

    //Define the playarea with the same starting card
    PlayArea pa = new PlayArea(card83, Side.FRONT);

    //Define the objective
    Objective objectiveResources = new SameCollectibleObjective(2,Resource.INSECT, 3);

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