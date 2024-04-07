package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.collectible.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    List<Card> cardList = List.of(
            new Card(
                    2,
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
                    3,
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
                    4,
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
                    5,
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