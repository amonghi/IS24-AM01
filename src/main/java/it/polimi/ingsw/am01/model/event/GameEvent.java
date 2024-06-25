package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.eventemitter.Event;

/**
 * This interface represents a generic event related to a specific {@code Game}.
 * These events reach all players connected to the specific game.
 * @see it.polimi.ingsw.am01.model.game.Game
 */
public interface GameEvent extends Event {
}
