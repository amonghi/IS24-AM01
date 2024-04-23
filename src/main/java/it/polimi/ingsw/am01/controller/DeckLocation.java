package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.model.game.Board;

/**
 * Represents the two possible decks from which the player can draw a card
 */
public enum DeckLocation {
    /**
     * @see Board#getResourceCardDeck()
     */
    RESOURCE_CARD_DECK,

    /**
     * @see Board#getGoldenCardDeck()
     */
    GOLDEN_CARD_DECK,
}
