package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

public record PlayerDisconnectedEvent(PlayerProfile pp) implements GameEvent {
}
