package it.polimi.ingsw.am01.client.gui.event;

import it.polimi.ingsw.am01.model.player.PlayerColor;

import java.util.List;
import java.util.Map;

public record SetPlayStatusEvent(List<String> players, Map<String, PlayerColor> colors,
                                 Map<String, Integer> scores, Map<String, Boolean> connections) implements ViewEvent {
}
