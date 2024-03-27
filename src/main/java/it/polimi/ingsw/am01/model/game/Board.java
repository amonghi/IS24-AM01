package it.polimi.ingsw.am01.model.game;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * The board game, shared among all players.
 * It contains {@code Deck} and {@code FaceUpCard}.
 *
 * @see it.polimi.ingsw.am01.model.game.Deck
 * @see it.polimi.ingsw.am01.model.game.FaceUpCard
 *
 */

public class Board {
    private final Set<FaceUpCard> faceUpResourceCards;
    private final Set<FaceUpCard> faceUpGoldenCards;
    private final Deck resourceCardDeck;
    private final Deck goldenCardDeck;

    /**
     * Constructs a new board with two Decks and four FaceUpCard.
     * @param faceUpResourceCards A set of two visible resource cards
     * @param faceUpGoldenCards A set of two visible golden cards
     * @param resourceCardDeck The resource card's deck
     * @param goldenCardDeck The golden card's deck
     *
     */
    public Board(Set<FaceUpCard> faceUpResourceCards, Set<FaceUpCard> faceUpGoldenCards, Deck resourceCardDeck, Deck goldenCardDeck) {
        this.faceUpResourceCards = new HashSet<>(faceUpResourceCards);
        this.faceUpGoldenCards = new HashSet<>(faceUpGoldenCards);
        this.resourceCardDeck = resourceCardDeck;
        this.goldenCardDeck = goldenCardDeck;
    }

    /**
     *
     * @return the reference of the set of the two visible resource cards on the board
     */
    public Set<FaceUpCard> getFaceUpResourceCards() {
        return faceUpResourceCards;
    }

    /**
     *
     * @return reference of the set of the two golden cards on the board
     */
    public Set<FaceUpCard> getFaceUpGoldenCards() {
        return faceUpGoldenCards;
    }

    /**
     *
     * @return the reference of the resource card's deck
     */
    public Deck getResourceCardDeck() {
        return resourceCardDeck;
    }

    /**
     *
     * @return the reference of the golden card's deck
     */
    public Deck getGoldenCardDeck() {
        return goldenCardDeck;
    }
}
