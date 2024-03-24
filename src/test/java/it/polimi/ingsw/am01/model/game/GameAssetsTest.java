package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameAssetsTest {

    @Test
    void getResourceCards() {
        List<Card> cards = GameAssets.getResourceCards();
        assertFalse(cards.isEmpty());
    }

    @Test
    void getGoldenCards() {
    }

    @Test
    void getStarterCards() {
    }

    @Test
    void getObjectives() {
    }
}