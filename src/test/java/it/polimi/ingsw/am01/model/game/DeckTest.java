package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.collectible.Resource;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    Card aCard = new Card(
            1,
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

    @Test
    void canCheckIfEmpty() {
        Deck deck = new Deck(GameAssets.getInstance().getStarterCards().stream().limit(1).collect(Collectors.toList()));
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