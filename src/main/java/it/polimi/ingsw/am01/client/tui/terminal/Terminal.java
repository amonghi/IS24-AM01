package it.polimi.ingsw.am01.client.tui.terminal;

import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;

/**
 * An interface that represents a terminal and provides methods to interact with it.
 * <p>
 * A terminal is also an {@link EventEmitter} that emits {@link ResizeEvent}s when the terminal is resized.
 *
 * @see TerminalFactory
 */
public interface Terminal extends EventEmitter<ResizeEvent> {
    /**
     * Enable raw mode on the terminal.
     * In raw mode, the terminal will not process input and will pass it directly to the program.
     */
    void enableRawMode();

    /**
     * Disabled raw mode by returning the terminal to its original state, before {@link #enableRawMode()} was called.
     * <p>
     * <b>Calling this method before {@link #enableRawMode()} leads to <i>undefined behaviour</i>.</b>
     */
    void disableRawMode();

    /**
     * Get the current dimensions of the terminal.
     *
     * @return the dimensions of the terminal
     */
    Dimensions getDimensions();
}
