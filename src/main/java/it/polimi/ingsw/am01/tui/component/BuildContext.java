package it.polimi.ingsw.am01.tui.component;

import it.polimi.ingsw.am01.tui.rendering.Renderer;

public class BuildContext {
    private final Renderer renderer;

    public BuildContext(Renderer renderer) {
        this.renderer = renderer;
    }

    public Renderer getRenderer() {
        return renderer;
    }
}
