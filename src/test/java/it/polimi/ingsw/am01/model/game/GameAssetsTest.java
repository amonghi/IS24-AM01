package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.objective.Objective;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameAssetsTest {

    @Test
    void getResourceCards() {
        List<Card> cards = GameAssets.getInstance().getResourceCards();
        assertFalse(cards.isEmpty());
    }

    @Test
    void getGoldenCards() {
        List<Card> cards = GameAssets.getInstance().getGoldenCards();
        assertFalse(cards.isEmpty());
    }

    @Test
    void getStarterCards() {
        List<Card> cards = GameAssets.getInstance().getStarterCards();
        assertFalse(cards.isEmpty());
    }

    @Test
    void getObjectives() {
        List<Objective> objectives = GameAssets.getObjectives();
        assertFalse(objectives.isEmpty());
    }
}