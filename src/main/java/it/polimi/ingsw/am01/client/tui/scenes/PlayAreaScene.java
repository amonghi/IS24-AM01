package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.PlayAreaComponent;

public class PlayAreaScene extends Composition {

    private final TuiView view;

    public PlayAreaScene(TuiView view) {
        this.view = view;
    }

    @Override
    protected Component compose() {
        return new PlayAreaComponent(
                view.getPlayAreaScrollX(),
                view.getPlayAreaScrollY(),
                view.getPlacements(view.getFocusedPlayer().orElse(view.getPlayerName()))
        );
    }
}
