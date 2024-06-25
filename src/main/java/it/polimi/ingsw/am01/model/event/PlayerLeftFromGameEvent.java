package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted by the {@code GameManager} class
 * when a player disconnects from a {@code Game}.
 * This event is generated from the {@link PlayerLeftEvent} event.
 * The event then reaches all {@code VirtualView} instances.
 * @param playerProfile the player who has disconnected
 * @param game the game in which the player was
 * @see it.polimi.ingsw.am01.model.game.GameManager
 * @see it.polimi.ingsw.am01.controller.VirtualView
 * @see PlayerLeftEvent
 */
public record PlayerLeftFromGameEvent(PlayerProfile playerProfile, Game game) implements GameManagerEvent {
}
