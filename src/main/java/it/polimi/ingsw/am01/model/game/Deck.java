package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;

import java.util.*;

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

    /**
     * Creates a copy of {@code this} and queue the cards stored in {@code newCards}
     * @return Returns a new {@code Deck}
     */
    public Deck createMergedDeck(List<Card> newCards) {
        List<Card> newCardList = new ArrayList<>(cards);
        newCardList.addAll(cards.size(), newCards);
        return new Deck(newCardList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder cards = new StringBuilder();
        for (Card card : this.cards) {
            cards.append(card.id()).append(", ");
        }
        cards.delete(cards.length() - 2, cards.length());
        return "Deck{" +
                "size= " + this.cards.size() +
                ", cards= " + cards +
                "}";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Deck deck = (Deck) other;
        return Objects.equals(cards, deck.cards);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(cards);
    }
}