package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

public record PlayerJoinedEvent(PlayerProfile player) implements GameEvent {
}
