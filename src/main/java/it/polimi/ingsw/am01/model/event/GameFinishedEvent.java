package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.Map;

public record GameFinishedEvent(Map<PlayerProfile, Integer> playersScores) implements GameEvent {
}
