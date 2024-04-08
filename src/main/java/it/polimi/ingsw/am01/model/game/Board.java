package it.polimi.ingsw.am01.model.game;

import java.util.Collections;
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
    private final Set<FaceUpCard> faceUpCards;
    private final Deck resourceCardDeck;
    private final Deck goldenCardDeck;

    /**
     * Constructs a new board with two Decks and four FaceUpCard.
     * @param resourceCardDeck The resource card's deck
     * @param goldenCardDeck The golden card's deck
     *
     */
    public Board(Deck resourceCardDeck, Deck goldenCardDeck) {
        this.resourceCardDeck = resourceCardDeck;
        this.goldenCardDeck = goldenCardDeck;
        this.faceUpCards = new HashSet<>();

        //resource cards
        this.faceUpCards.add(new FaceUpCard(resourceCardDeck, goldenCardDeck));
        this.faceUpCards.add(new FaceUpCard(resourceCardDeck, goldenCardDeck));

        //golden cards
        this.faceUpCards.add(new FaceUpCard(goldenCardDeck, resourceCardDeck));
        this.faceUpCards.add(new FaceUpCard(goldenCardDeck, resourceCardDeck));
    }

    /**
     *
     * @param resourceCardDeck The resource card's deck
     * @param goldenCardDeck The golden card's deck
     * @return a new Board with shuffled decks
     */
    public static Board createShuffled(Deck resourceCardDeck, Deck goldenCardDeck) {
        resourceCardDeck.shuffle();
        goldenCardDeck.shuffle();
        return new Board(resourceCardDeck, goldenCardDeck);
    }

    /**
     *
     * @return the reference of the set of the four visible cards on the board
     */
    public Set<FaceUpCard> getFaceUpCards() {
        return Collections.unmodifiableSet(faceUpCards);
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
