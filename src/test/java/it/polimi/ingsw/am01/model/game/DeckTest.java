package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.collectible.Resource;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    Card aCard = new Card(
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
    );
    List<Card> cardList = List.of(
            new Card(
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
            ),
            new Card(
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
            ),
            new Card(
                    CardColor.GREEN,
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
                            Map.of(Resource.PLANT, 1)
                    )
            )

    );

    @RepeatedTest(10)
    void canShuffle() {
        Deck deck = new Deck(cardList);
        //deck.shuffle();
        List<Card> newList = new ArrayList<>();
        while (!deck.isEmpty()) {
            newList.addFirst(deck.draw().get());
        }
        assertEquals(cardList, newList);
    }

    @Test
    void canCheckIfEmpty() {
        Deck deck = new Deck(List.of(aCard));
        assertFalse(deck.isEmpty());
        deck.draw();
        assertTrue(deck.isEmpty());
    }

    @Test
    void canDraw() {
        Deck deck = new Deck(List.of(aCard));
        assertFalse(deck.isEmpty());
        Optional<Card> drawnCard;
        drawnCard = deck.draw();
        assertTrue(drawnCard.isPresent());
        assertEquals(aCard, drawnCard.get());
        assertTrue(deck.draw().isEmpty());
    }
}