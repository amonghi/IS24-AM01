package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.collectible.Resource;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FaceUpCardTest {
    List<Card> shortList = List.of(new Card(
            CardColor.RED,
            true,
            false,
            new FrontCardFace(
                    Corner.empty(),
                    Corner.filled(Resource.FUNGI),
                    Corner.missing(),
                    Corner.filled(Resource.FUNGI)
            ),
            new BackCardFace(
                    Corner.empty(),
                    Corner.empty(),
                    Corner.empty(),
                    Corner.empty(),
                    Map.of(Resource.FUNGI, 1)
            )
    ));

    List<Card> longList = List.of(new Card(
                    CardColor.BLUE,
                    true,
                    false,
                    new FrontCardFace(
                            Corner.empty(),
                            Corner.filled(Resource.FUNGI),
                            Corner.missing(),
                            Corner.filled(Resource.FUNGI)
                    ),
                    new BackCardFace(
                            Corner.empty(),
                            Corner.empty(),
                            Corner.empty(),
                            Corner.empty(),
                            Map.of(Resource.ANIMAL, 1)
                    )
            ),
            new Card(
                    CardColor.PURPLE,
                    true,
                    false,
                    new FrontCardFace(
                            Corner.empty(),
                            Corner.filled(Resource.FUNGI),
                            Corner.missing(),
                            Corner.filled(Resource.FUNGI)
                    ),
                    new BackCardFace(
                            Corner.empty(),
                            Corner.empty(),
                            Corner.empty(),
                            Corner.empty(),
                            Map.of(Resource.INSECT, 1)
                    )
            ));

    @Test
    void canReplenishFromMainSource() {
        Deck mainDeck = new Deck(longList);
        Deck auxDeck = new Deck(shortList);
        FaceUpCard faceUpCard = new FaceUpCard(mainDeck, auxDeck);
        Optional<Card> seenCard = faceUpCard.getCard();
        Optional<Card> drawnCard = faceUpCard.draw();
        assertTrue(seenCard.isPresent());
        assertTrue(drawnCard.isPresent());
        assertEquals(seenCard.get(), drawnCard.get());

        assertTrue(mainDeck.isEmpty());
    }

    @Test
    void canReplenishFromAuxiliarySource() {
        Deck mainDeck = new Deck(new ArrayList<>());
        Deck auxDeck = new Deck(longList);
        FaceUpCard faceUpCard = new FaceUpCard(mainDeck, auxDeck);
        Optional<Card> seenCard = faceUpCard.getCard();
        Optional<Card> drawnCard = faceUpCard.draw();
        assertTrue(seenCard.isPresent());
        assertTrue(drawnCard.isPresent());
        assertEquals(seenCard.get(), drawnCard.get());

        assertTrue(auxDeck.isEmpty());
    }

    @Test
    void isEmptyWhenBothSourceAreEmpty() {
        Deck mainDeck = new Deck(new ArrayList<>());
        Deck auxDeck = new Deck(shortList);
        FaceUpCard faceUpCard = new FaceUpCard(mainDeck, auxDeck);
        Optional<Card> seenCard = faceUpCard.getCard();
        Optional<Card> drawnCard = faceUpCard.draw();
        assertTrue(seenCard.isPresent());
        assertTrue(drawnCard.isPresent());
        assertEquals(seenCard.get(), drawnCard.get());

        assertTrue(faceUpCard.getCard().isEmpty());
    }
}