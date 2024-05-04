package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.eventemitter.EventEmitterImpl;
import it.polimi.ingsw.am01.eventemitter.EventListener;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.event.FaceUpCardReplacedEvent;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a slot that contains a completely visible Card and that replenish itself from source "mainSource" when drawn. In case mainSource is empty, the card will be taken from "auxiliarySource"
 */
public class FaceUpCard implements DrawSource, EventEmitter<FaceUpCardReplacedEvent> {
    transient private final EventEmitterImpl<FaceUpCardReplacedEvent> emitter;
    private final Deck mainSource;
    private final Deck auxiliarySource;
    private Card card;

    /**
     * Constructs a new FaceUpCard
     *
     * @param mainSource      The main deck where from where the cards are drawn to replenish the slot
     * @param auxiliarySource The auxiliary deck where from where the cards are drawn to replenish the slot (when main deck is empty)
     */
    public FaceUpCard(Deck mainSource, Deck auxiliarySource) {
        this.emitter = new EventEmitterImpl<>();
        this.mainSource = mainSource;
        this.auxiliarySource = auxiliarySource;
        this.card = drawFromDecks();
    }

    /**
     * Draws the card from the slot and replenish it with a new drawn card from main Deck, if present, or auxiliary Deck if not
     *
     * @return Returns the drawn card
     * If the card slot is empty, it returns {@code Optional.empty()}
     */
    @Override
    public Optional<Card> draw() {
        Optional<Card> drawnCard = Optional.ofNullable(card);
        card = drawFromDecks();
        emitter.emit(new FaceUpCardReplacedEvent(this));
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

    /**
     * Draws a card from no-empty source (starting from mainSource)
     *
     * @return A card drawn from mainSource or auxiliarySource if the first one is empty. If both source are empty it returns null
     */
    private Card drawFromDecks() {
        return mainSource.draw().or(auxiliarySource::draw).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        FaceUpCard faceUpCard = (FaceUpCard) other;
        return card.equals(faceUpCard.card) && mainSource.equals(faceUpCard.mainSource) && auxiliarySource.equals(faceUpCard.auxiliarySource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "FaceUpCard{" +
                "card=" + card +
                ", mainSource=" + mainSource +
                ", auxiliarySource=" + auxiliarySource +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(card, mainSource, auxiliarySource);
    }

    @Override
    public Registration onAny(EventListener<FaceUpCardReplacedEvent> listener) {
        return emitter.onAny(listener);
    }

    @Override
    public <T extends FaceUpCardReplacedEvent> Registration on(Class<T> eventClass, EventListener<T> listener) {
        return emitter.on(eventClass, listener);
    }

    @Override
    public boolean unregister(Registration registration) {
        return emitter.unregister(registration);
    }
}
