package it.polimi.ingsw.am01.model.game;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardTest {

    Deck resDeck = new Deck(GameAssets.getInstance().getResourceCards());
    Deck goldenDeck = new Deck(GameAssets.getInstance().getGoldenCards());

    @Test
    public void testNotShuffledBoard() {
        Board standardBoard = new Board(resDeck, goldenDeck);
        assertEquals(resDeck, standardBoard.getResourceCardDeck());
        assertEquals(goldenDeck, standardBoard.getGoldenCardDeck());

        assertEquals(4, standardBoard.getFaceUpCards().size());
        assertEquals(2, standardBoard.getFaceUpCards().stream().filter(fc -> !Objects.requireNonNull(fc.getCard().orElse(null)).isGold()).collect(Collectors.toSet()).size());
        assertEquals(2, standardBoard.getFaceUpCards().stream().filter(fc -> Objects.requireNonNull(fc.getCard().orElse(null)).isGold()).collect(Collectors.toSet()).size());
    }

    @Test
    public void testShuffledBoard() {
        Board shuffledBoard = Board.createShuffled(resDeck, goldenDeck);

        assertEquals(resDeck, shuffledBoard.getResourceCardDeck());
        assertEquals(goldenDeck, shuffledBoard.getGoldenCardDeck());

        assertEquals(4, shuffledBoard.getFaceUpCards().size());
        assertEquals(2, shuffledBoard.getFaceUpCards().stream().filter(fc -> !Objects.requireNonNull(fc.getCard().orElse(null)).isGold()).collect(Collectors.toSet()).size());
        assertEquals(2, shuffledBoard.getFaceUpCards().stream().filter(fc -> Objects.requireNonNull(fc.getCard().orElse(null)).isGold()).collect(Collectors.toSet()).size());
    }
}