package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted by the {@code GameManager} class when a player joins a game.
 * @param playerProfile the player who has joined
 * @param game the game in which the player has joined
 * @see it.polimi.ingsw.am01.model.game.GameManager
 */
public record PlayerJoinedInGameEvent(PlayerProfile playerProfile, Game game) implements GameManagerEvent {
}
