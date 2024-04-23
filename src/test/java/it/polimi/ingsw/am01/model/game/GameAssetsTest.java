package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.objective.Objective;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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
        List<Objective> objectives = GameAssets.getInstance().getObjectives();
        assertFalse(objectives.isEmpty());
    }

    @Test
    void getCardById() {
        List<Card> cards = GameAssets.getInstance().getResourceCards();
        assertFalse(cards.isEmpty());
        int id = 1;

        Optional<Card> firstCard = GameAssets.getInstance().getCardById(1);
        assertTrue(firstCard.isPresent());
        Optional<Card> firstCardFromList = cards.stream().filter(card -> card.id() == id).findAny();
        assertTrue(firstCardFromList.isPresent());

        Optional<Card> invalidCard = GameAssets.getInstance().getCardById(2000);
        assertTrue(invalidCard.isEmpty());
    }

    @Test
    void getObjectiveById() {
        List<Objective> objectives = GameAssets.getInstance().getObjectives();
        assertFalse(objectives.isEmpty());

        int id = 1;

        Optional<Objective> firstObjective = GameAssets.getInstance().getObjectiveById(id);
        assertTrue(firstObjective.isPresent());
        Optional<Objective> firstObjectiveFromList = objectives.stream().filter(objective -> objective.getId() == id).findAny();
        assertTrue(firstObjectiveFromList.isPresent());

        assertEquals(firstObjectiveFromList.get(), firstObjective.get());

        Optional<Objective> invalidObjective = GameAssets.getInstance().getObjectiveById(2000);
        assertTrue(invalidObjective.isEmpty());
    }

}