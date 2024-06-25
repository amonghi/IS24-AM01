package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.Centered;
import it.polimi.ingsw.am01.client.tui.component.layout.Column;

import java.util.List;

public class WelcomeScene extends Composition {
    @Override
    public Component compose() {
        return Centered.both(
                new Column(List.of(
                        Centered.horizontally(new Text("Welcome to Codex Naturalis.")),
                        Centered.horizontally(new Text("Use the connect command to connect to a server. Type 'show manual' for commands detail"))
                ))
        );
    }
}
