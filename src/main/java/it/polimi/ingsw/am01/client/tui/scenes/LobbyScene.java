package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.tui.TuiView;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.Centered;
import it.polimi.ingsw.am01.client.tui.component.layout.Column;

import java.util.stream.Collectors;

public class LobbyScene implements Composition {

    private final TuiView view;

    public LobbyScene(TuiView view) {
        this.view = view;
    }

    public Component compose() {
        return Centered.both(new Column(
                view.getPlayersInGame().stream()
                        .map(Text::new)
                        .collect(Collectors.toList())
        ));
    }
}
