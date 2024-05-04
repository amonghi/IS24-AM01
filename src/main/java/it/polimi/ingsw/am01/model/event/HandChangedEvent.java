package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.card.Card;

import java.util.Set;

public record HandChangedEvent(Set<Card> currentHand) implements GameEvent {
}
