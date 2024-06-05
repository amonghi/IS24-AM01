package it.polimi.ingsw.am01.client.gui.event;

import it.polimi.ingsw.am01.client.gui.model.Placement;

import java.util.SortedSet;

public record SetPlayAreaEvent(SortedSet<Placement> placements) implements ViewEvent {
}
