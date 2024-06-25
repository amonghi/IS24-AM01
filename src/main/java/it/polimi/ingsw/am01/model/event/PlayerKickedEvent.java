package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted by the {@code Game} class after a player disconnection.
 * The {@code VirtualView} instance of the disconnected player receives the event
 * and removes the reference to the previously saved game.
 * @param player the player who has disconnected
 * @see it.polimi.ingsw.am01.controller.VirtualView
 */
public record PlayerKickedEvent(PlayerProfile player) implements GameEvent {
}
