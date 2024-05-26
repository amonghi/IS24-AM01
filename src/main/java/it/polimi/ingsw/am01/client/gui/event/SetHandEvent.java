package it.polimi.ingsw.am01.client.gui.event;

import java.util.Set;

public record SetHandEvent(Set<Integer> hand) implements ViewEvent {
}
