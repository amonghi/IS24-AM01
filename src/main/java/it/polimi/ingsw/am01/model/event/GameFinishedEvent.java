package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.Map;

/**
 * This event is emitted when a game ends normally
 * or when it ends due to player disconnections (only one player connected).
 * This event communicates the final results of the match.
 * @param playersScores a {@link Map} that contains the final scores
 */
public record GameFinishedEvent(Map<PlayerProfile, Integer> playersScores) implements GameEvent {
}
