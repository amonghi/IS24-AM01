package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

public record PlayerJoinedEvent(Game game, PlayerProfile player) implements GameEvent {
}
