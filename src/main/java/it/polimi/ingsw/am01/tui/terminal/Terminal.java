package it.polimi.ingsw.am01.tui.terminal;

import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.tui.rendering.Dimensions;

public interface Terminal extends EventEmitter<ResizeEvent> {
    void enableRawMode();

    void disableRawMode();

    Dimensions getDimensions();
}
