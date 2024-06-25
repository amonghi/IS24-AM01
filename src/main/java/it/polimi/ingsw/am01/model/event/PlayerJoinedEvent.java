package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted by the {@code Game} class when a player joins a game.
 * @param player the player who has joined
 * @see it.polimi.ingsw.am01.model.game.Game#join(PlayerProfile) Game.join(player)
 */
public record PlayerJoinedEvent(PlayerProfile player) implements GameEvent {
}
