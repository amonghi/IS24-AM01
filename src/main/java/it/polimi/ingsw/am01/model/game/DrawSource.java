package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;

import java.util.Optional;

/**
 * DrawSource represents a source from where drawing cards.
 * There are two different draw source: {@link Deck} and {@link FaceUpCard}
 * @see it.polimi.ingsw.am01.model.game.Deck
 * @see it.polimi.ingsw.am01.model.game.FaceUpCard
 */
public interface DrawSource {
    /**
     * This method permits to draw a card
     * @return {@code Optional.empty()} if source is empty, otherwise the first card of source.
     * Implementations of this method are defined in {@link Deck} and {@link FaceUpCard}
     */
    Optional<Card> draw();
}