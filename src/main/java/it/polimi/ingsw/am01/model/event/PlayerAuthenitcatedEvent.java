package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.eventemitter.Event;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

public record PlayerAuthenitcatedEvent(PlayerProfile playerProfile) implements Event {
}
