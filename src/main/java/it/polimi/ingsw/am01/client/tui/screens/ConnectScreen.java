package it.polimi.ingsw.am01.client.tui.screens;

import it.polimi.ingsw.am01.client.tui.component.BuildContext;
import it.polimi.ingsw.am01.client.tui.component.ComponentBuilder;
import it.polimi.ingsw.am01.client.tui.component.Composition;
import it.polimi.ingsw.am01.client.tui.component.elements.Text;
import it.polimi.ingsw.am01.client.tui.component.layout.Centered;
import it.polimi.ingsw.am01.client.tui.component.layout.Column;

public class ConnectScreen extends Composition {

    public static ComponentBuilder builder() {
        return ConnectScreen::new;
    }

    protected ConnectScreen(BuildContext context) {
        super(context);
    }

    @Override
    protected ComponentBuilder compose() {
        return Centered.both(
                Column.of(
                        Text.of("Welcome to Codex Naturalis."),
                        Text.of("Use the connect command to connect to a server.")
                )
        );
    }
}
