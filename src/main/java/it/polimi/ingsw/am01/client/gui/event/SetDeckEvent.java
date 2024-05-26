package it.polimi.ingsw.am01.client.gui.event;

import it.polimi.ingsw.am01.model.card.CardColor;

import java.util.Optional;

public record SetDeckEvent(Optional<CardColor> goldenDeck, Optional<CardColor> resourceDeck, boolean goldenDeckIsEmpty,
                           boolean resourceDeckIsEmpty) implements ViewEvent {
}
