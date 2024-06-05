package it.polimi.ingsw.am01.client.tui.terminal;

import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;

public interface Terminal extends EventEmitter<ResizeEvent> {
    void enableRawMode();

    void disableRawMode();

    Dimensions getDimensions();
}
