package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted by the {@code Game} class when a player disconnects
 * from a game during the following stages:
 *     <li>{@code AWAITING_PLAYERS}</li>
 *     <li>{@code SETUP_STARTING_CARD_SIDE}</li>
 *     <li>{@code SETUP_COLOR}</li>
 *     <li>{@code SETUP_OBJECTIVE}.</li>
 * The player will no longer be part of the match.
 * @param player the player who has disconnected
 * @see it.polimi.ingsw.am01.model.game.GameStatus
 */
public record PlayerLeftEvent(PlayerProfile player) implements GameEvent {
}
