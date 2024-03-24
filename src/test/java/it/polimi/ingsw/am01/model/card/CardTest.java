package it.polimi.ingsw.am01.model.card;

import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.collectible.Resource;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

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
            1,
            CardColor.RED,
            true,
            false,
            aCardFF,
            aCardBF
    );
    @Test
    void canGetBackFace() {
        assertEquals(aCardBF, aCard.getFace(Side.BACK));
    }

    @Test
    void canGetFrontFace() {
        assertEquals(aCardFF, aCard.getFace(Side.FRONT));
    }
}