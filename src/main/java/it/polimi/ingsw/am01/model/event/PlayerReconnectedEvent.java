package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted when a player reconnects to a game.
 * @param pp the player who has reconnected
 * @see it.polimi.ingsw.am01.model.game.Game#handleReconnection(PlayerProfile) Game.handleReconnection(player)
 */
public record PlayerReconnectedEvent(PlayerProfile pp) implements GameEvent {
}
