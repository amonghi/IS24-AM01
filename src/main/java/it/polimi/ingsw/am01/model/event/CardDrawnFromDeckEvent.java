package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Deck;

public record CardDrawnFromDeckEvent(Deck resourceCardDeck, Deck goldenCardDeck) implements GameEvent {

}
