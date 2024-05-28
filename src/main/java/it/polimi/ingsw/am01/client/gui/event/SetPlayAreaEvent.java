package it.polimi.ingsw.am01.client.gui.event;

import it.polimi.ingsw.am01.client.gui.model.GUIPlacement;

import java.util.SortedSet;

public record SetPlayAreaEvent(SortedSet<GUIPlacement> guiPlacements) implements ViewEvent {
}
