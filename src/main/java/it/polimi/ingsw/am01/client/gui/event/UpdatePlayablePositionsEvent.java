package it.polimi.ingsw.am01.client.gui.event;

import it.polimi.ingsw.am01.client.gui.Position;

import java.util.List;

public record UpdatePlayablePositionsEvent(List<Position> playablePositions) implements ViewEvent {
}
