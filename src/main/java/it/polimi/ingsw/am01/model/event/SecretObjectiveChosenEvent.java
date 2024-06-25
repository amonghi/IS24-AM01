package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.Set;

/**
 * This event is emitted when a player chooses his secret {@code Objective}.
 * @param playersHaveChosen a {@link Set} that contains all players who have chosen
 */
public record SecretObjectiveChosenEvent(Set<PlayerProfile> playersHaveChosen) implements GameEvent {
}
