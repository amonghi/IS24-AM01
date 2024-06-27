package it.polimi.ingsw.am01.client.tui.scenes;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.elements.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.Centered;
import it.polimi.ingsw.am01.client.tui.component.layout.Column;

import java.util.List;

/**
 * This scene permits players to authenticate with the server.
 */
public class AuthScene extends Composition {

    /**
     * {@inheritDoc}
     */
    @Override
    public Component compose() {
        return Centered.both(
                new Column(List.of(
                        new Text("Use the authenticate command to choose a username.")
                ))
        );
    }
}
