package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents an ordered deck of Cards
 */
public class Deck implements DrawSource {
    private final List<Card> cards;

    /**
     * Constructs a new Deck from a list of Cards
     *
     * @param cards A reference to the list of cards from which the deck is composed
     */

    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    /**
     * Shuffle the cards inside the Deck
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * @return Returns whether the Deck is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Draws the last Card removing it from the Deck
     *
     * @return Returns the drawn card
     * If the deck is empty, it returns {@code Optional.empty()}
     */
    public Optional<Card> draw() {
        Card element = null;
        if (!cards.isEmpty()) {
            element = cards.remove(cards.size() - 1);
        }
        return Optional.ofNullable(element);
    }
}