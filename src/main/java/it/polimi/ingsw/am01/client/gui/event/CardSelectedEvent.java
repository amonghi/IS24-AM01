package it.polimi.ingsw.am01.client.gui.event;

import it.polimi.ingsw.am01.model.card.Side;

public record CardSelectedEvent(int id, Side side) implements ViewEvent {
}
