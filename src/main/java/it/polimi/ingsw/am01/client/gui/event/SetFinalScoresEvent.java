package it.polimi.ingsw.am01.client.gui.event;

import java.util.Map;

public record SetFinalScoresEvent(Map<String, Integer> finalScores) implements ViewEvent {
}
