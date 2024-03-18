package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.game.Deck;
import it.polimi.ingsw.am01.model.game.DrawSource;

import java.util.Optional;

/**
 * Represents a slot that contains a completely visible Card and that replenish itself from a Deck when drawn
 */
public class FaceUpCard implements DrawSource {
    private Card card;
    private final Deck source;

    /**
     * Constructs a new FaceUpCard
     *
     * @param source The deck where from where the cards are drawn to replenish the slot
     */
    public FaceUpCard(Deck source) {
        this.source = source;
        this.card = source.draw().orElse(null);
    }

    /**
     * Draws the card from the slot and replenish it with a new drawn card from the Deck, if present
     *
     * @return Returns the drawn card
     * If the card slot is empty, it returns {@code Optional.empty()}
     */
    @Override
    public Optional<Card> draw() {
        Optional<Card> drawnCard = Optional.ofNullable(card);
        card = source.draw().orElse(null);
        return drawnCard;
    }

    /**
     * Shows the card without removing it from the slot
     *
     * @return Returns the current card
     * If the card slot is empty, it returns {@code Optional.empty()}
     */
    public Optional<Card> getCard() {
        return Optional.ofNullable(card);
    }
}
