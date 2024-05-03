package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Deck;

public class CardDrawnFromDeckEvent extends GameEvent {

    private final Deck resourceCardDeck;
    private final Deck goldenCardDeck;

    public CardDrawnFromDeckEvent(Deck resourceCardDeck, Deck goldenCardDeck) {
        this.resourceCardDeck = resourceCardDeck;
        this.goldenCardDeck = goldenCardDeck;
    }


    public Deck getResourceCardDeck() {
        return resourceCardDeck;
    }

    public Deck getGoldenCardDeck() {
        return goldenCardDeck;
    }
}
