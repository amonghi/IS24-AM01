package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.eventemitter.Event;

/**
 * This interface represents a generic event related to the {@code GameManager}.
 * These events reach all players connected to the server ({@code GameManager} class is instantiated only once).
 */
public interface GameManagerEvent extends Event {
}
