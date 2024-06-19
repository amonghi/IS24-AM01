package it.polimi.ingsw.am01.client.tui.terminal;

import it.polimi.ingsw.am01.eventemitter.Event;

/**
 * An event that is emitted when the {@link Terminal} is resized.
 */
public record ResizeEvent() implements Event {
}
