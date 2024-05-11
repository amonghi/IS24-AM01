package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.Set;

public record SecretObjectiveChosenEvent(Set<PlayerProfile> playersHaveChosen) implements GameEvent {
}
