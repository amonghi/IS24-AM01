package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted when a player disconnects from a game.
 * @param pp the player who has disconnected
 * @see it.polimi.ingsw.am01.model.game.Game#handleDisconnection(PlayerProfile) Game.handleDisconnection(player)
 */
public record PlayerDisconnectedEvent(PlayerProfile pp) implements GameEvent {
}
