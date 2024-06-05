package it.polimi.ingsw.am01.client.gui.event;

import java.util.List;

public record SetObjectives(List<Integer> objectives, int secretObjective) implements ViewEvent {
}
