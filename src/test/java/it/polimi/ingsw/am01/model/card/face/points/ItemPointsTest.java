package it.polimi.ingsw.am01.model.card.face.points;

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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ItemPointsTest {

    final Points points = new ItemPoints(Item.INKWELL, 2);
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
            1,
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
            Corner.filled(Resource.FUNGI),
            points
    );
    final BackCardFace aCardBF = new BackCardFace(
            Corner.empty(),
            Corner.filled(Item.INKWELL),
            Corner.empty(),
            Corner.filled(Item.INKWELL),
            Map.of(Resource.FUNGI, 1)
    );
    final Card aCard = new Card(
            2,
            CardColor.RED,
            true,
            false,
            aCardFF,
            aCardBF
    );
    @Test
    void canCalculateScoredPoints() {
        PlayArea playArea = new PlayArea(starterCard, Side.FRONT);
        PlayArea.CardPlacement cardPlacement = playArea.placeAt(1, 0, aCard, Side.BACK);
        assertEquals(4, points.calculateScoredPoints(cardPlacement));
    }
}