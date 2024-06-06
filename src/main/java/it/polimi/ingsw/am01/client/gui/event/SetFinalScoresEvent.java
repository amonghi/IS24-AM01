package it.polimi.ingsw.am01.client.gui.event;

import it.polimi.ingsw.am01.model.player.PlayerColor;

import java.util.Map;

public record SetFinalScoresEvent(Map<String, Integer> finalScores, Map<String, PlayerColor> playerColors) implements ViewEvent {
}
